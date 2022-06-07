## gift формат тестового задания

`` :: имя вопроса :: текст вопроса {``

    ответы
``}``
``#комментарий для тестируемого``

_Между собой вопросы разделяются как минимум одной пустой строкой_

Для деления по категориям испольуется:

``$CATEGORY: категория/подкатегория/подподкатегория``

### Служебные символы

__~__ __:__ __{__ __=__ __}__ __#__  

_В вопросах и ответах экранируются_ __\\__

Текст вопроса (только) может быть в одном из форматов `moodle` (Moodle Auto-Format), `html` (HTML format), `plain`
(Plain text format) and `markdown` (Markdown format). При необходимости указывается перед текстом вопроса как  ``[markdown]``

### типы вопросов

__множественный выбор__

``Who's buried in Grant's tomb?{~Grant ~Jefferson =no one}`` 

``Grant is {~buried =entombed ~living} in Grant's tomb.`` 

``Japanese characters originally came from what country? {`` 
`` ~India#Sorry. ``
`` =China#Correct! ``
`` ~Korea#Try again. ``
`` ~Egypt#That’s not it.`` 
``} ``

``::Jesus' hometown::Jesus Christ was from { ``
`` ~Jerusalem#This was an important city, but is wrong. ``
`` ~%25%Bethlehem#He was born here, but not raised here. ``
`` ~%50%Galilee#You need to be more specific. ``
`` =Nazareth#Yes! That's right! ``
``}``

__короткие ответы__

``o's buried in Grant's tomb?{=no one =nobody} ``

``Two plus two equals {=four =4}. ``
``Who's buried in Grant's tomb? { ``
`` =no one#excellent answer!`` 
`` =nobody#excellent answer! ``
``} ``

``::Jesus' hometown:: Jesus Christ was from { ``
`` =Nazareth#Yes! That's right! ``
`` =%75%Nazereth#Right, but misspelled. ``
`` =%25%Bethlehem#He was born here, but not raised``
``here. ``
``}``

__правильно-неправильно__

``Grant is buried in Grant's tomb.{F} ``

``The sun rises in the east.{TRUE} ``

``Grant is buried in Grant's tomb.``

__соотвествия__

``Match the following countries with their corresponding capitals. { ``
`` =Canada -> Ottawa ``
`` =Italy -> Rome ``
`` =Japan -> Tokyo ``
`` =India -> New Delhi ``
``}``

__числовой__

``When was Ulysses S. Grant born? {#1822} ``

``What is the value of pi (to 3 decimal places)? ``
``{#3.1415:0.0005}. ``

``What is the value of pi (to 3 decimal places)? ``
``{#3.141..3.142}. ``

``When was Ulysses S. Grant born? {# ``
`` =1822:0 ``
`` =%50%1822:2 ``
``} ``

__множественный выбор__

``What two people are entombed in Grant's tomb? { ``
`` ~No one ``
`` ~%50%Grant ``
`` ~%50%Grant's wife`` 
`` ~Grant's father ``
``} ``

``What two people are entombed in Grant's tomb? { ``
`` ~%-50%No one`` 
`` ~%50%Grant ``
`` ~%50%Grant's wife ``
`` ~%-50%Grant's father ``
``} ``
