import os, torch
from transformers import AutoModelForSequenceClassification, AutoTokenizer
from model.modules.utils import load_yaml
import pandas as pd
import numpy as np
from tqdm import tqdm

# Root directory
PROJECT_DIR = os.path.dirname(os.path.abspath(os.path.dirname(__file__)))

# Load config
config_path = os.path.join(PROJECT_DIR, 'model', 'config', 'inference_config.yaml')
config = load_yaml(config_path)

def load_model(select_model, maxlen, loss, device):
    name = f'{select_model}_{maxlen}_{"".join(loss.split())}'
    checkpoint_path = os.path.join(PROJECT_DIR, 'model', 'KoBERT_26_CrossEntropyLoss')
    pretrained_link = config.MODEL.pretrained_link[select_model]
    num_of_classes = config.MODEL.num_of_classes

    # Load model and tokenizer without training
    try:
        model = AutoModelForSequenceClassification.from_pretrained(checkpoint_path, num_labels=num_of_classes).to(device)
        tokenizer = AutoTokenizer.from_pretrained(pretrained_link)
    except Exception as e:
        raise RuntimeError(f"Error loading model or tokenizer: {e}")

    return model, tokenizer

def inference(data, max_seq_len, model, tokenizer, device, topk=False, k=5):
    items = []

    if isinstance(data, str):
        item = {key: torch.tensor(val).to(device) for key, val in tokenizer(
            data,
            truncation=True,
            padding='max_length',
            max_length=max_seq_len
        ).items()}
        items.append(item)
    elif isinstance(data, pd.DataFrame):
        for name in tqdm(data['업체명_r']):
            item = {key: torch.tensor(val).to(device) for key, val in tokenizer(
                name,
                truncation=True,
                padding='max_length',
                max_length=max_seq_len
            ).items()}
            items.append(item)

    model.eval()
    with torch.no_grad():
        outputs = model(**{k: torch.stack([item[k] for item in items]) for k in items[0]})
        probs = torch.nn.functional.softmax(outputs.logits, dim=-1)
        top_val, top_ind = torch.topk(probs, k=k, dim=1)
        top_val = top_val.cpu().numpy()
        top_ind = top_ind.cpu().numpy()

    if isinstance(data, str):
        if topk:
            return np.array(pd.DataFrame(top_ind).replace(config.LABELING.keys(), config.LABELING.values())), top_val
        else:
            return config.LABELING[top_ind[0][0]], top_val[0][0]
    elif isinstance(data, pd.DataFrame):
        data['업종'] = top_ind[:, 0]
        data['업종'] = data['업종'].replace(config.LABELING.keys(), config.LABELING.values())
        return data, top_val
