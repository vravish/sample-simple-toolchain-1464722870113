FROM websphere-liberty:webProfile7
EXPOSE 9080
ADD ${appLocation}/target/WHCProjectWebApp.war /config/dropins/
