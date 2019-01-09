while true; do

    echo
    echo
    echo Things are bad. People need help.
    psql --command='update mission set current_status='Assigned' where mission_id in (select mission_id from mission where current_status='Requested' order by last_update desc limit 20);' -h postgresql.naps-emergency-response.svc

#update mission set current_status='Pickedup' where mission_id in (select mission_id from mission where current_status='Assigned' order by last_update desc limit 20);
#update mission set current_status='Rescued' where mission_id in (select mission_id from mission where current_status='Pickedup' order by last_update desc limit 20);
#update mission set current_status='Cancelled' where mission_id in (select mission_id from mission where current_status='Assigned' order by last_update desc limit 10);
#update mission set current_status='Requested' where mission_id in (select mission_id from mission where current_status='Cancelled' order by last_update desc limit 10);
#update mission set current_status='Requested' where mission_id in (select mission_id from mission where current_status='Rescued' order by last_update desc limit 20);

    sleep 5
done
