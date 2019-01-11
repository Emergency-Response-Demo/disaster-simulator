# NOTE: Set these variables before running this script
#   POSTGRESQL_USER
#   POSTGRESQL_HOSTNAME
#   POSTGRESQL_DATABASE

DELAY=1

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

function reset_simulation() {
    echo "Resetting simulation"
    sql "update mission set current_status='Requested';"
}

function delay() {
    sleep ${DELAY}
}


echo "Simulator starting"

while true; do

    reset_simulation

    i="0"
    while [ $i -lt 160 ]; do
        change_status "Requested"  "Assigned"  1
        delay
        change_status "Assigned"  "Pickedup"  1
        delay
        change_status "Pickedup"  "Rescued"  1
        delay
        echo
        i=$[i+1]
    done

done
