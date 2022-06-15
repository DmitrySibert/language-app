Nginx general setup

log_format custom   '$remote_addr - $remote_user [$time_local] '
'"$request" $status $body_bytes_sent '
'"$http_referer" "$http_user_agent" "$http_x_forwarded_for" rt=$request_time urt=$upstream_response_time';
access_log /var/log/nginx/access.log custom;
error_log /var/log/nginx/error.log;