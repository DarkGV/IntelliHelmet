echo "Updating local repo"
git pull origin master

echo "Sending files with message $1"
git add .
git commit -m "$1"
git push origin master
