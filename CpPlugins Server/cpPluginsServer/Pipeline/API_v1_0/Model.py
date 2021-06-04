## =========================================================================
## @author Leonardo Florez-Valencia (florez-l@javeriana.edu.co)
## =========================================================================

import base64, os
import cpPluginsServer.Pipeline.API_v1_0.Algorithms as Algorithms

'''
'''
class Pipeline:

  '''
  '''
  def __init__( self ):
    self.RamDisk = '/dev/shm'
  # end def

  '''
  '''
  def loadPipeline( self, desc ):
    # Inputs
    inputs = {}
    inputs_str = ''
    if 'inputs' in desc:
      c = 1
      for i in desc[ 'inputs' ]:
        name_ram = os.path.join( self.RamDisk, i[ 'name' ] ) + '.png'
        to_ram = open( name_ram, 'wb' )
        to_ram.write(
          base64.decodebytes( i[ 'raw_buffer' ].encode( 'utf-8' ) )
          )
        to_ram.close( )
        inputs[ 'i' + str( c ) ] = name_ram
        inputs_str += i[ 'name' ] + ' = i' + str( c ) + ', '
        c += 1
      # end for
    # end if

    # Algorithm
    if 'xml_description' in desc:
      pass
    elif 'filter_name' in desc:
      call_line  = 'Algorithms.' + desc[ 'filter_name' ] + '( '
      if inputs_str != '':
        call_line += inputs_str
      # end if

      # Properties
      if 'parameters' in desc:
        for p in desc[ 'parameters' ]:
          call_line += p[ 'name' ] + ' = ' + p[ 'value' ] + ', '
        # end for
      # end if
      call_line = call_line.strip( ' ' ).strip( ',' )
      call_line += ' )'

      output = eval( call_line, { 'Algorithms': Algorithms }, inputs )
      from_ram = open( output, 'rb' )
      encoded_output = base64.b64encode( from_ram.read( ) ).decode( 'utf-8' )
      from_ram.close( )
      return encoded_output
    # end if
  # end def
# end class

## eof - $RCSfile$
