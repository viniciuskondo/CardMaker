metadata = entry.getMetaData()
nColumns = metadata.getColumnCount()
i = 1
while i <= nColumns:
    print(metadata.getColumnName(i) + ': ' + entry.getObject(i))
    i += 1
