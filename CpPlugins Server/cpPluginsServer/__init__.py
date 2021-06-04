## =========================================================================
## @author Leonardo Florez-Valencia (florez-l@javeriana.edu.co)
## =========================================================================

import flask, flask_restful
from .Extensions import ma
from cpPluginsServer.Pipeline.API_v1_0.Resources import cpPluginsPipeline_v1_0_bp
from cpPluginsServer.Common.ErrorHandling import ObjectNotFound, AppErrorBaseClass

'''
'''
def create_service( settings ):
  service = flask.Flask( __name__ )
  service.config.from_object( settings )

  # Init extensions
  ma.init_app( service )

  # Catch all 404 errors and ignore ending '/' in URLs
  flask_restful.Api( service, catch_all_404s = True )
  service.url_map.strict_slashes = False

  # Blueprints (connection points)
  service.register_blueprint( cpPluginsPipeline_v1_0_bp )

  # Personalized error handlers
  register_error_handlers( service )

  return service
# end def

'''
'''
def register_error_handlers( service ):

  '''
  '''
  @service.errorhandler( Exception )
  def handle_exception_error( e ):
    return flask.jsonify(
      { 'msg': 'Internal server error => ' + str( e ) }
      ), 500
  # end def

  '''
  '''
  @service.errorhandler( 405 )
  def handle_405_error( e ):
    return flask.jsonify( { 'msg': 'Method not allowed' } ), 405
  # end def

  '''
  '''
  @service.errorhandler( 403 )
  def handle_403_error( e ):
    return flask.jsonify( { 'msg': 'Forbidden error' } ), 403
  # end def

  '''
  '''
  @service.errorhandler( 404 )
  def handle_404_error( e ):
    return flask.jsonify( { 'msg': 'Not Found error' } ), 404
  # end def

  '''
  '''
  @service.errorhandler( AppErrorBaseClass )
  def handle_app_base_error( e ):
    return flask.jsonify( { 'msg': str( e ) } ), 500
  # end def

  '''
  '''
  @service.errorhandler( ObjectNotFound )
  def handle_object_not_found_error( e ):
    return flask.jsonify( { 'msg': str( e ) } ), 404
  # end def
# end def

## eof - $RCSfile$
