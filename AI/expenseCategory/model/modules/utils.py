from datetime import datetime
import yaml
from attrdict import AttrDict

def load_yaml(path):
    with open(path, 'r', encoding='UTF8') as f:
        return AttrDict(yaml.load(f, Loader=yaml.FullLoader))

def str2dt(str_):
    return datetime.strptime(str(str_), '%Y%m%d')
