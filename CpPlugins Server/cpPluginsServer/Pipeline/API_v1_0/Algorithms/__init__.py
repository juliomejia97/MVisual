## =========================================================================
## @author Leonardo Florez-Valencia (florez-l@javeriana.edu.co)
## =========================================================================

import os
import itk

'''
'''
def OtsuMultipleThresholdsImageFilter( **kwargs ):
  i = itk.ImageFileReader.New( FileName = kwargs[ 'Input' ] )

  l = itk.RGBToLuminanceImageFilter.New( Input = i.GetOutput( ) )

  t = itk.OtsuMultipleThresholdsImageFilter.New( Input = l.GetOutput( ) )
  if 'NumberOfHistogramBins' in kwargs:
    t.SetNumberOfHistogramBins( int( kwargs[ 'NumberOfHistogramBins' ] ) )
  # end if
  if 'NumberOfThresholds' in kwargs:
    t.SetNumberOfThresholds( int( kwargs[ 'NumberOfThresholds' ] ) )
  # end if
  if 'ValleyEmphasis' in kwargs:
    t.SetValleyEmphasis( bool( kwargs[ 'ValleyEmphasis' ] ) )
  # end if
  if 'ReturnBinMidpoint' in kwargs:
    t.SetReturnBinMidpoint( bool( kwargs[ 'ReturnBinMidpoint' ] ) )
  # end if

  c = itk.LabelToRGBImageFilter.New( Input = t.GetOutput( ) )

  base_fname = os.path.splitext( kwargs[ 'Input' ] )
  out_fname = base_fname[ 0 ] + '_Output'
  if base_fname[ 1 ] == '':
    out_fname += '.mha'
  else:
    out_fname += base_fname[ 1 ]
  # end if

  w = itk.ImageFileWriter.New( Input = c.GetOutput( ), FileName = out_fname )
  w.Update( )

  return out_fname
# end def

'''
'''
def AnisotropicDiffusionImageFilter( **kwargs ):
  i = itk.ImageFileReader[ itk.Image[ itk.F, 2 ] ].New( FileName = kwargs[ 'Input' ] )
  
  t = itk.AnisotropicDiffusionImageFilter.New( Input = i.GetOutput( ) )
  
  r = itk.RescaleIntensityImageFilter.New( Input = t.GetOutput( ) )
  r.SetOutputMinimum( 0 )
  r.SetOutputMaximum( 255 )
  
  c = itk.CastImageFilter[ itk.Image[ itk.F, 2 ], itk.Image[ itk.UC, 2 ] ].New( Input = r.GetOutput( ) )
  
  base_fname = os.path.splitext( kwargs[ 'Input' ] )
  out_fname = base_fname[ 0 ] + '_Output'
  if base_fname[ 1 ] == '':
    out_fname += '.mha'
  else:
    out_fname += base_fname[ 1 ]
  # end if

  w = itk.ImageFileWriter.New( Input = c.GetOutput( ), FileName = out_fname )
  w.Update( )

  return out_fname
# end def

## eof - $RCSfile$
