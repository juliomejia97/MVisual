## =========================================================================
## @author Leonardo Florez-Valencia (florez-l@javeriana.edu.co)
## =========================================================================

from marshmallow import fields
import cpPluginsServer.Extensions

'''
'''
class PipelineSchema( cpPluginsServer.Extensions.ma.Schema ):
  id = fields.Integer( dump_only = True )
  packages = fields.String( )
  filter_name = fields.String( )
  xml_description = fields.String( )
  inputs = fields.Nested( 'InputSchema', many = True )
  parameters = fields.Nested( 'ParameterSchema', many = True )
# end class

'''
'''
class InputSchema( cpPluginsServer.Extensions.ma.Schema ):
  id = fields.Integer( dump_only = True )
  name = fields.String( )
  data_format = fields.String( )
  data_type = fields.String( )
  dimensions = fields.String( )
  origin = fields.String( )
  spacing = fields.String( )
  direction = fields.String( )
  raw_buffer = fields.String( )
# end class

'''
'''
class ParameterSchema( cpPluginsServer.Extensions.ma.Schema ):
  id = fields.Integer( dump_only = True )
  name = fields.String( )
  value = fields.String( )
# end class

## eof - $RCSfile$
