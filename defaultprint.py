metadata = entry.getMetaData()
nColumns = metadata.getColumnCount()
i = 1
output = ""
while i <= nColumns:
    output += metadata.getColumnName(i) + ': ' + entry.getString(i) + ' '
    i += 1
print(output)
