# create an empty portal db 
# and populate it with essential lookup data

UNAME=root
DBNAME=portal

# clean install?
for arg in "$@"
do
  case $arg in
    -c | --clean | clean )
	echo "Clean install. Remove existing tables."
	mysql -u $UNAME mysql -e "drop database $DBNAME;"
	mysql -u $UNAME mysql -e "create database $DBNAME;"
	echo "-> done"
    ;;
  esac
done  

echo "Creating new tables in $DBNAME ..."
mysql -u $UNAME $DBNAME < ../../portal-core/db/portal.sql
    
echo "Inserting countries ..."
cd ../data/countries
cp *.txt  /tmp/countries
cd ../../db
mysql -u $UNAME $DBNAME < countries/import.sql

echo "Inserting networks ..."
cd ../data/networks
cp *.txt  /tmp/networks
cd ../../db
mysql -u $UNAME $DBNAME < networks/import.sql
