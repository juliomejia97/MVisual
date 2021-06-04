import importlib

#print( "=====================" )
#print( type( globals( )[ 'itk' ].ImageFileReader ) )
#print( "=====================" )
#print( type( globals( )[ 'itk' ].OtsuMultipleThresholdsImageFilter ) )
#print( "=====================" )
#print( type( globals( )[ 'itk' ].OtsuMultipleThresholdsImageFilter ) )
#print( "=====================" )

itk = importlib.import_module( 'itk' )

## v = eval( 'itk.ImageFileReader.New.__code__.co_varnames', { 'itk': itk } )
##v = eval( 'dir( itk.ImageFileReader.ICD2 )', { 'itk': itk } )
##print( v )

#r = eval( 'itk.ImageFileReader.New( FileName = fn )', { 'itk': itk }, { 'fn': '/dev/shm/Input.png' } )
#l = eval( 'itk.RGBToLuminanceImageFilter.New( Input = i )', { 'itk': itk }, { 'i': r.GetOutput( ) } )
#f = eval( 'itk.OtsuMultipleThresholdsImageFilter.New( Input = i )', { 'itk': itk }, { 'i': l.GetOutput( ) } )
#w = eval( 'itk.ImageFileWriter.New( Input = i, FileName = \'/dev/shm/Output.png\' )', { 'itk': itk }, { 'i': f.GetOutput( ) } )
#eval( 'w.Update( )' )


# print( globals( )[ 'itk' ][ 'itk.ImageFileReader' ] )
# print( globals( )[ 'itk.OtsuMultipleThresholdsImageFilter' ] )

#r = itk.ImageFileReader.New( FileName = '/dev/shm/Input.png' )
#r.Update( )
#f = itk.OtsuMultipleThresholdsImageFilter.New( Input = r.GetOutput( ) )
