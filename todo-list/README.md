To do list (RPC demo)
---------------------

To run server:

```shell
python3 -m venv venv # first time use only
. venv/bin/activate
pip install -r requirements.txt
export FLASK_ENV=development # enables automatic code reloading
flask run
```

Then open http://127.0.0.1:5000/ in a web browser.

To demo accessing the app from the command line:

```shell
curl -i http://127.0.0.1:5000/tasks
curl -i -F text=hello http://127.0.0.1:5000/tasks
```
