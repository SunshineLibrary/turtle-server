host=$1
callback="callback=Angular_1.1.5"

echo "host:"+$host
echo "callback:"+$callback

do_get()
{
    echo "GET "$2
    curl "$1$2"
    echo ""
    echo "complete"
}

do_get $host "/exercise/v1/root?$callback"
do_get $host "/exercise/v1/chapters/1011?ts=1&act=status&$callback"
