msg = "$1"

echo "Updating local repo"
git pull origin master

echo "Sending files with message $1"
git add .
git commit -m msg
git push origin master
