import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

def read_excel():
    df = pd.read_excel('ALVR-2017.xls', header=[0,1], sheet_name='2017 Special General')
    return df

def drop_totals(df):
    df = df.iloc[1:] # drop race totals row
    df = df.drop('Total Active & Inactive', axis=1)
    df = df.drop('Federally-Registered (may be of any race)', axis=1, level=1)
    df = df.drop('Total Inactive', axis=1, level=1)
    df = df.drop('Total Active', axis=1, level=1)
    return df

def drop_other_non_id(df):
    df = df.drop('Other', axis=1, level=1)
    df = df.drop('Not Identified', axis=1, level=1)
    return df

def prepare_data(df):
    df = drop_totals(df)
    df = drop_other_non_id(df)
    return df

df = read_excel()
df = prepare_data(df)

#df['Black Active:Inactive'] = df['Active']['Black'] / df['Inactive']['Black']
#df['White Active:Inactive'] = df['Active']['White'] / df['Inactive']['White']
#df['D_WAI_BAI'] = df['White Active:Inactive'] - df['Black Active:Inactive']
#df['log(White:Black)'] = np.log(df['Active']['White'] / df['Active']['Black'])
#df.hist(bins=50, figsize=(20,15))
#df = df.ix[:, ['Black Active:Inactive','White Active:Inactive','log(White:Black)', 'D_WAI_BAI']]
#df = df.ix[:, ['D_WAI_BAI']]
#df = df.ix[:, ['Black Active:Inactive','White Active:Inactive']]

print(df.head())

df_active = df['Active']

df_active.plot(kind='bar', stacked=True)
plt.show()
