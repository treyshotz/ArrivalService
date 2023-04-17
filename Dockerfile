#Create MySQL Image for JSP Tutorial Application
FROM mysql
MAINTAINER mads.lundegaard@live.no

ENV MYSQL_ROOT_PASSWORD password

EXPOSE 3306
