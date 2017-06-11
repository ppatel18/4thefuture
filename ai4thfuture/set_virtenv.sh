# sudo apt-get install python-pip
# sudo pip install --upgrade pip
# sudo pip install virtualenv

virtualenv .venv -p python3
source .venv/bin/activate
pip install -r requirements
