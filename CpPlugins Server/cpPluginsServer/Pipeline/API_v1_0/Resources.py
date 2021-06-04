## =========================================================================
## @author Leonardo Florez-Valencia (florez-l@javeriana.edu.co)
## =========================================================================

import flask, flask_restful
from .Schemas import *
from .Model import *
import cpPluginsServer.Pipeline.API_v1_0.Algorithms as Algorithms

cpPluginsPipeline_v1_0_bp = flask.Blueprint( 'cpPluginsPipeline_v1_0_bp', __name__ )
pipeline_schema = PipelineSchema( )
service = flask_restful.Api( cpPluginsPipeline_v1_0_bp )

'''
'''
class PipelineResource( flask_restful.Resource ):

  AllAlgorithms = []

  '''
  '''
  def __init__( self ):
    self.Model = Pipeline( )
    if len( PipelineResource.AllAlgorithms ) == 0:
      for a in dir( Algorithms ):
        if a[ 0 ] != '_' and a != 'itk' and a != 'os':
          PipelineResource.AllAlgorithms.append( a )
        # end if
      # end for
    # end if
  # end def

  '''
  '''
  def get( self ):
    return PipelineResource.AllAlgorithms
  # end def

  '''
  '''
  def post( self ):
    data = flask.request.get_json( )
    pipeline_dict = pipeline_schema.load( data )
    return self.Model.loadPipeline( pipeline_dict )
  # end def
# end class

service.add_resource(
  PipelineResource, '/api/v1.0/pipeline/', endpoint='pipeline_resource'
  )

## eof - $RCSfile$
