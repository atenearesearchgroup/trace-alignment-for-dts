import numpy as np
import pandas as pd


def clean_df(df: pd.DataFrame, columns):
    cleaned_df = pd.DataFrame()
    for c in columns:
        cleaned_df.insert(0, c, df.loc[:, c], True)

    for name, _ in cleaned_df.iteritems():
        cleaned_df[name].replace(' ', np.nan, inplace=True)
        cleaned_df.dropna(subset=[name], inplace=True)
        cleaned_df[name].update(pd.to_numeric(cleaned_df[name], errors='coerce'))

    return cleaned_df
