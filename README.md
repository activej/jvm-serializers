##### Ser Time+Deser Time (ns)
![sorted by time](https://chart.googleapis.com/chart?cht=bhs&chs=600x174&chd=t:228,240,280,538,527,697,870,588|318,319,351,649,711,701,571,1106&chco=5d99f9,4d89f9&chdlp=t&chbh=15&chds=0,1694&chxr=1,0,1694&chxt=y,x&chxl=0:|json%2Fdsl-json%2Fdatabind|protobuf|kryo-registered-flat|json-array%2Fdsl-json%2Fdatabind|protostuff|colfer|activej|activej-speed "Ser Time+Deser Time (ns)")

##### Size, Compressed size [light] in bytes
![sorted by size](https://chart.googleapis.com/chart?cht=bhs&chs=600x174&chd=t:140,137,144,152,153,152,187,271|78,91,96,89,89,90,109,217&chco=5d99f9,4d89f9&chdlp=t&chbh=15&chds=0,488&chxr=1,0,488&chxt=y,x&chxl=0:|json%2Fdsl-json%2Fdatabind|json-array%2Fdsl-json%2Fdatabind|protobuf|protostuff|colfer|activej-speed|activej|kryo-registered-flat "Size, Compressed size [light] in bytes")


|<sub>*measures are in *ns*</sub>  |create|ser|deser|total|size|+dfl|
|----------------------------------|------|---|-----|-----|----|----|
|activej-speed                     |   70 |228|  318|  546| 240| 144|
|activej                           |   51 |240|  319|  559| 228| 137|
|colfer                            |   74 |280|  351|  631| 241| 152|
|protostuff                        |   88 |538|  650| 1187| 242| 153|
|json-array/dsl-json/databind      |   53 |527|  711| 1238| 296| 187|
|kryo-registered-flat              |   58 |697|  701| 1398| 218| 140|
|protobuf                          |  207 |870|  572| 1441| 242| 152|
|json/dsl-json/databind            |   52 |588| 1106| 1694| 488| 271|


Look in the "tpc" directory for source code