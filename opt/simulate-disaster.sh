# NOTE: Set these variables before running this script
#   POSTGRESQL_USER
#   POSTGRESQL_HOSTNAME
#   POSTGRESQL_DATABASE

DELAY=5

function sql() {
    sql=$1
    psql -U ${POSTGRESQL_USER} -h ${POSTGRESQL_HOSTNAME} -d ${POSTGRESQL_DATABASE} --command="${sql}"
}

function change_status() {
    from_status=$1
    to_status=$2
    count=$3

    echo "${from_status} -> ${to_status} (${count})"
    sql "update mission set current_status='${to_status}' where mission_id in (select mission_id from mission where current_status='${from_status}' limit ${count});"

}

function delay() {
    sleep ${DELAY}
}


echo "Simulation starting"

echo "Setting status of all Missions to 'Requested'"
sql "update mission set current_status='Requested';"

change_status("Requested", "Assigned", 20)
change_status("Requested", "Pickedup", 20)
change_status("Requested", "Rescued", 20)
change_status("Requested", "Cancelled", 20)

while true; do

    echo
    change_status("Requested", "Assigned", 20)
    delay()
    change_status("Assigned", "Pickedup", 15)
    delay()
    change_status("Assigned", "Cancelled", 5)
    delay()
    change_status("Pickedup", "Rescued", 15)
    delay()
    change_status("Rescued", "Requested", 15)
    delay()
    change_status("Cancelled", "Requested", 5)
    delay()
    

done
