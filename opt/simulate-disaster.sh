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

GLOBAL_RANDOM=1
function update_global_random() {
    low=$1
    high=$2

    let "GLOBAL_RANDOM = $RANDOM % ($high + 1) + $low"
}

# Apply the specified status transition to several incedents, with the count and specific incidents chosen randomly
change_status_randomly() {
    from_status=$1
    to_status=$2
    max_count=$3

    update_global_random 0 ${max_count}
    change_status ${from_status} ${to_status}  ${GLOBAL_RANDOM}
}


echo "Simulator starting"

while true; do

    reset_simulation

    i="0"
    while [ $i -lt 180 ]; do

        change_status_randomly "Requested" "Assigned" 3
        delay
        change_status_randomly "Assigned" "Pickedup" 2
        delay
        change_status_randomly "Pickedup" "Rescued" 2
        delay
        echo
        i=$[i+1]
    done

done
