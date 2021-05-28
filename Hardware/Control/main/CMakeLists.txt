# ability.1
# srcs
#set(COMPONENT_SRCS "app_camera.c" "app_wifi.c" "app_main.c" "app_spi.c")
# includes
# set(COMPONENT_ADD_INCLUDEDIRS "${project_dir}/include")
# set(COMPONENT_REQUIRES )

# ability.2
#idf_build_get_property(project_dir PROJECT_DIR)
idf_component_register(SRCS "app_main.c" "app_spi.c" "app_wifi.c"
                    INCLUDE_DIRS "${project_dir}/include")
                    
# extra functions
# OTA and identify
# idf_component_register(EMBED_TXTFILES ${project_dir}/ServerCerts/otakey.pub)
