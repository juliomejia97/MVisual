# Mvisual

MVisual en su primera versión es un aplicativo móvil para la visualización y procesamiento de imágenes diagnósticas que provengan desde la nube (mediante Firebase) y el almacenamiento del dispositivo.

## Tabla de Contenidos

## Guía de Instalación

Este documento sirve para la primera versión de MVisual.

Pasos para la instalación de la aplación

1. Descargar la última versión de Android Studio, versión 4.2.1  

    [](https://developer.android.com/studio?hl=es-419&gclid=CjwKCAjw47eFBhA9EiwAy8kzNJw6AIG4Alf8IceHV0N0v71tt-9ndBTlassbRPCWWRDPNfR6NvpbHxoCAxMQAvD_BwE&gclsrc=aw.ds)

2. Clonar el repositorio en su computador
3. Al momento de abrir Android Studio, importar el proyecto

    ![MVisual%20ReadMe%201ea9a600212a414f8ddbc4c7cdb278f5/Untitled.png](MVisual%20ReadMe%201ea9a600212a414f8ddbc4c7cdb278f5/Untitled.png)

    Seleccionar "Open an Existing Project" y buscar en la ruta que se almacenó el repositorio

4. Una vez que el proyecto ha abierto, se debe descargar el emulador de un dispositivo Android, para esta versión se recomiendas descargar Pixel 3a. 
5. Dar Click en la pestaña Tools → AVD Mananager→Create Virtual Device

    ![MVisual%20ReadMe%201ea9a600212a414f8ddbc4c7cdb278f5/Untitled%201.png](MVisual%20ReadMe%201ea9a600212a414f8ddbc4c7cdb278f5/Untitled%201.png)

6. Una vez seleccionado, se debe descargar el software para el emulador. Recomendamos API Level 28 en adelante.

    ![MVisual%20ReadMe%201ea9a600212a414f8ddbc4c7cdb278f5/Untitled%202.png](MVisual%20ReadMe%201ea9a600212a414f8ddbc4c7cdb278f5/Untitled%202.png)

7. Una vez descargado, dar click a Next y la siguiente pantalla a Finish.
8. Es posible que durante la instalación Android Studio le solicite instalar NDK, acepte esta instalación debido a que es necesaria para el funcionamiento de MVisual. Para verificar que las dependencias se han instalado correctamente se recomienda verificar al **SDK Manager.** Debe aparecer de la siguiente forma:

    ![MVisual%20ReadMe%201ea9a600212a414f8ddbc4c7cdb278f5/Untitled%203.png](MVisual%20ReadMe%201ea9a600212a414f8ddbc4c7cdb278f5/Untitled%203.png)

    En caso de faltar alguno de estos de click en el check boz y descarguelos

9. Para correr la aplicación es importante realizar los siguientes tres pasos:

    ![MVisual%20ReadMe%201ea9a600212a414f8ddbc4c7cdb278f5/Untitled%204.png](MVisual%20ReadMe%201ea9a600212a414f8ddbc4c7cdb278f5/Untitled%204.png)

    1. Como se muestra en la cinta dar click a Sync with Gradle.
    2. Una vez finalizado este proceso dar click a Build.
    3. Verifique en el emulador seleccionado es Pixel 3a API XXX. Esto se encuentra al lado del botón Run.
    4. Una vez finalizado dar click a Run.

    ## Guía de Configuración Servidor CpPlugins

    Para configurar el acceso al servidor desde la aplicación verifique con los administradores del sistema si el servidor se encuentra en linea, de lo contrario, ponga a funcionar el servidor en su máquina local y cambie las siguientes lineas de código.

    1.  En el paquete com.example.pixelmanipulation.model y en la clase ProcessingMethodActivity, cambie la **linea 43**, cambie el url por el local host. 
    2. Asi mismo en el paquete com.providers y el la clase CpPluginsProvider, modifique la **línea 32**.
