#!/bin/python

import fileinput

def process_food_names(s):
    # clean up the & nonsense
    s.replace('&', ' and ')
    if s[-1] == '\n':
        return s[0:-1]
    return s

id_to_name_mapping = { }
for lines in fileinput.input():
    food_id, food_name = lines.split('\001')
    id_to_name_mapping[food_id] = process_food_names(food_name)

print '<?xml version="1.0" encoding="utf-8"?>'
print '<resources>'
id_to_name_keys = id_to_name_mapping.keys()
num_arrays = len(id_to_name_keys) / 500 + 1
food_id = 0
for array_num in range(0, num_arrays):
    print '<string-array name="food_array_'+str(array_num)+'">'
    key_list = []
    if array_num < num_arrays - 1:
        limit = 500
    else:
        limit = len(id_to_name_mapping) % 500
    for i in range(0, limit):
        index = array_num * 500 + i
        key = id_to_name_keys[i]
        print '<item>' + id_to_name_mapping[key] + '</item>'
        key_list.append(key)
    print '</string-array>'
    print '<integer-array name="food_id_' + str(array_num)+'">'
    for key in key_list:
        print '<item>' +key+'</item>'
    print '</integer-array>'
    print '<integer name="food_array_size">'+str(num_arrays)+'</integer>'
print '</resources>'
