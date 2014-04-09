#!/bin/python

import fileinput

def strip_newline(s):
    if s[-1] == '\n':
        return s[0:-1]
    return s

id_to_name_mapping = { }
for lines in fileinput.input():
    food_id, food_name = lines.split('\001')
    id_to_name_mapping[food_id] = strip_newline(food_name)

print '<?xml version="1.0" encoding="utf-8"?>'
print '<resources>'
print '<string-array name="food_array">'
key_list = []
for key in id_to_name_mapping.keys():
    print '<item>' + id_to_name_mapping[key] + '</item>'
    key_list.append(key)
print '</string-array>'
print '<int-array name="food_id">'
for key in key_list:
    print '<item>' +key+'</item>'
print '</int-array>'
print '</resources>'
