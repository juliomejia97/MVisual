## =========================================================================
## @author Leonardo Florez-Valencia (florez-l@javeriana.edu.co)
## =========================================================================

import os
import cpPluginsServer

server_settings = os.getenv( 'APP_SETTINGS_MODULE' )
cpPlugins_server = cpPluginsServer.create_service( server_settings )

## eof - $RCSfile$
