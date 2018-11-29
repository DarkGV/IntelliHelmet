set /p message=Select Message: 
echo "Updating local repo"
git pull origin master

echo "Sending files with message %message%"
git add .
git commit -m %message%
git push origin master
