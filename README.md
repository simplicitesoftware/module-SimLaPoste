<!--
 ___ _            _ _    _ _    __
/ __(_)_ __  _ __| (_)__(_) |_ /_/
\__ \ | '  \| '_ \ | / _| |  _/ -_)
|___/_|_|_|_| .__/_|_\__|_|\__\___|
            |_| 
-->
![](https://docs.simplicite.io//logos/logo250.png)
* * *

`SimLaPoste` module definition
==============================

Datanova La Poste

`SimLpPostalCode` business object definition
--------------------------------------------

https://datanova.laposte.fr/datasets/laposte-hexasmal

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `lpCpPostalCode`                                             | char(20)                                 |          | yes       |          | -                                                                                |
| `lpCpInseeCode`                                              | char(50)                                 | yes*     | yes       |          | -                                                                                |
| `lpCpTownName`                                               | text(1000)                               |          | yes       |          | -                                                                                |

