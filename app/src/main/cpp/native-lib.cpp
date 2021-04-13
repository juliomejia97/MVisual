#include <jni.h>
#include <string>
#include <string.h>
#include <cmath>
#include <cstdlib>
#include <fstream>
#include <functional>
#include <iomanip>
#include <iostream>
#include <regex>
#include <sstream>
#include <vector>

// -------------------------------------------------------------------------
void read_file_into_buffer(std::string &buffer, const std::string &fname)
{
    std::ifstream input(fname.c_str());
    input.seekg(0, std::ios::end);
    buffer.reserve(input.tellg());
    input.seekg(0, std::ios::beg);
    buffer.assign(
            (std::istreambuf_iterator<char>(input)),
            std::istreambuf_iterator<char>());
    input.close();
}

// -------------------------------------------------------------------------
std::vector<std::string> tokenize(const std::string &s, const std::regex &r)
{
    std::sregex_token_iterator i{s.begin(), s.end(), r, -1};
    std::vector<std::string> t{i, {}};
    t.erase(
            std::remove_if(
                    t.begin(), t.end(),
                    [](const std::string &v) { return (v.size() == 0); }),
            t.end());
    return (t);
}

// -------------------------------------------------------------------------
template <class _TPixel>
void convert_buffer(
        const std::string &fname, const std::vector<int> &dims,
        double window, double level,
        unsigned char **wl_buffer)
{
    std::string buffer;
    read_file_into_buffer(buffer, fname);

    unsigned long number_of_pixels = 1;
    for (unsigned int i = 0; i < dims.size(); ++i)
        number_of_pixels *= dims[i];

    double m = 0, b = 0;
    if (window > 0)
    {
        m = double(255) / window;
        b = double(255) * (0.5 - (level / window));
    } // end if

    *wl_buffer = new unsigned char[number_of_pixels];
    unsigned char *oIt = *wl_buffer;
    const _TPixel *iIt = reinterpret_cast<const _TPixel *>(buffer.data());
    for (unsigned long i = 0; i < number_of_pixels; ++i)
    {
        double v = (m * double(*iIt)) + b;
        if (v < 0)
            *oIt = 0;
        else if (v > 255)
            *oIt = 255;
        else
            *oIt = (unsigned char)(v);
        oIt++;
        iIt++;
    } // end for
}

// -------------------------------------------------------------------------
std::string convertString(JNIEnv *env, jstring jStr){
    const char *cstr = env->GetStringUTFChars(jStr, NULL);
    if (!jStr)
        return "";

    const jclass stringClass = env->GetObjectClass(jStr);
    const jmethodID getBytes = env->GetMethodID(stringClass, "getBytes", "(Ljava/lang/String;)[B");
    const jbyteArray stringJbytes = (jbyteArray) env->CallObjectMethod(jStr, getBytes, env->NewStringUTF("UTF-8"));

    size_t length = (size_t) env->GetArrayLength(stringJbytes);
    jbyte* pBytes = env->GetByteArrayElements(stringJbytes, NULL);

    std::string ret = std::string((char *)pBytes, length);
    env->ReleaseByteArrayElements(stringJbytes, pBytes, JNI_ABORT);

    env->DeleteLocalRef(stringJbytes);
    env->DeleteLocalRef(stringClass);

    return ret;
}




extern "C"
JNIEXPORT jobjectArray JNICALL
Java_com_example_pixelmanipulation_MainActivity_convertMHD(JNIEnv *env, jobject thiz, jstring mhdFile, jstring rawFile) {
    using TBufferFunction =
    std::function<void(const std::string &, const std::vector<int> &, double, double, unsigned char **)>;

    // Read MHD file
    std::string mhd_buffer;
    read_file_into_buffer(mhd_buffer, convertString(env, mhdFile));
    std::istringstream mhd_stream(mhd_buffer);

    // Parse it
    std::string line, raw_fname;
    std::vector<int> dims;
    TBufferFunction raw_function;
    std::regex re(R"([\s|,|=]+)");
    while (std::getline(mhd_stream, line))
    {
        std::vector<std::string> tokens = tokenize(line, re);
        if (tokens[0] == "CompressedData")
        {
            if (tokens[1] != "False")
            {
                std::cerr
                        << "Could not load compressed data. Finishing."
                        << std::endl;
                return NULL;
            } // end if
        }
        else if (tokens[0] == "DimSize")
        {
            for (unsigned int i = 1; i < tokens.size(); ++i)
                dims.push_back(std::atoi(tokens[i].c_str()));
            while (dims.size() < 3)
                dims.push_back(1);
        }
        else if (tokens[0] == "ElementType")
        {
            if (tokens[1] == "MET_CHAR")
                raw_function = convert_buffer<char>;
            else if (tokens[1] == "MET_SHORT")
                raw_function = convert_buffer<short>;
            else if (tokens[1] == "MET_INT")
                raw_function = convert_buffer<int>;
            else if (tokens[1] == "MET_LONG")
                raw_function = convert_buffer<long>;
            else if (tokens[1] == "MET_UNSIGNED_CHAR")
                raw_function = convert_buffer<unsigned char>;
            else if (tokens[1] == "MET_UNSIGNED_SHORT")
                raw_function = convert_buffer<unsigned short>;
            else if (tokens[1] == "MET_UNSIGNED_INT")
                raw_function = convert_buffer<unsigned int>;
            else if (tokens[1] == "MET_UNSIGNED_LONG")
                raw_function = convert_buffer<unsigned long>;
            else if (tokens[1] == "MET_FLOAT")
                raw_function = convert_buffer<float>;
            else if (tokens[1] == "MET_DOUBLE")
                raw_function = convert_buffer<double>;
            else
            {
                std::cerr
                        << "Could not load data: type not recognized. Finishing."
                        << std::endl;
                return NULL;
            } // end if
        }
        else if (tokens[0] == "ElementDataFile")
            raw_fname = tokens[1];
    } // end while

    // Process buffer
    double window = 2048;
    double level = 0;
    unsigned char *wl_buffer;
    raw_function(convertString(env, rawFile), dims, window, level, &wl_buffer);

    // Save PGM files to vector
    unsigned long ndigits = (unsigned long)(std::ceil(std::log10(dims[2])));
    unsigned char *wlIt = wl_buffer;
    std::vector<std::vector<int>> images(dims[2]);
    for (unsigned long z = 0; z < dims[2]; ++z)
    {

        for (int j = 0; j < dims[1]; ++j)
        {
            for (int i = 0; i < dims[0]; ++i)
                images[z].push_back(int(*(wlIt++)));
        } // end for
    }   // end for

    for (unsigned long z = 0; z < dims[2]; ++z)
    {
        std::cout << "Image #: " << (z + 1) << " " << images[z].size();
    }

    // Finish
    delete wl_buffer;
    
    return NULL;
}
