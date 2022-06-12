package com.dfd.plugin.generator.code.services;

import com.dfd.plugin.generator.code.model.DFDModel;
import com.dfd.plugin.generator.code.model.blocks.*;
import com.dfd.plugin.generator.code.model.blocks.cells.*;
import com.dfd.plugin.generator.code.model.blocks.cells.maincreator.*;
import com.dfd.plugin.generator.code.model.blocks.parsed.*;
import com.dfd.plugin.generator.code.model.collections.*;
import com.dfd.plugin.generator.code.model.collections.comparators.LineComparator;
import com.dfd.plugin.generator.code.services.generator.code.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.googlejavaformat.java.FormatterException;

import java.util.*;

public class GeneratorCode {

    private DFDModel dfdModel;

    private Method workProcessStart;
    private ClassGenerator workProcess;
    private InterfaceGenerator componentImpl;

    public GeneratorCode(DFDModel dfdModel){
        this.dfdModel = dfdModel;
    }

    public Multimap<TypeFolders, GeneralGenerator> startGenerateCode() {
        // распарсиваем входные данные
        SpecialList list = parseDFDModel();
        // генерируем код
        Multimap<TypeFolders, GeneralGenerator> code = generationCode(list);
        // возвращаем код
        return code;
    }

    private SpecialList parseDFDModel(){
        HashMap<Integer, CellText> cells = new HashMap<>(); // все блоки
        ArrayList<Line> lines = new ArrayList<>(); // все стрелки, соединяющие блоки

        Long lastKeyProcesses = 0L;
        // INPUT, OUTPUT BLOCKS //
        int indexInput = -1;
        int indexOutput = -2;
        CellConstants type = CellConstantsCreator.getStyleConstant("input;"); // получаем константу для стиля блока
        CellText cellTxt = new CellText(dfdModel.getInputText(), type); // создаем текстовые данные блока
        cells.put(indexInput, cellTxt); // помещаем в карту по id
        type = CellConstantsCreator.getStyleConstant("output;"); // получаем константу для стиля блока
        cellTxt = new CellText(dfdModel.getOutputText(), type); // создаем текстовые данные блока
        cells.put(indexOutput, cellTxt); // помещаем в карту по id
        // ALL PROCESSES //
        for(Map.Entry<Long, String> entry : dfdModel.getProcesses().entrySet()) {
            type = CellConstantsCreator.getStyleConstant("shape=ellipse;"); // получаем константу для стиля блока
            cellTxt = new CellText(entry.getValue(), type); // создаем текстовые данные блока
            cells.put(Math.toIntExact(entry.getKey()), cellTxt); // помещаем в карту по id
            lastKeyProcesses = entry.getKey();
        }
        // EXTERNALS STORAGES //
        for(Map.Entry<Long, String> entry : dfdModel.getExternalStorages().entrySet()) {
            type = CellConstantsCreator.getStyleConstant("shape=saveDataBox;"); // получаем константу для стиля блока
            cellTxt = new CellText(entry.getValue(), type); // создаем текстовые данные блока
            cells.put(Math.toIntExact(entry.getKey()), cellTxt); // помещаем в карту по id
        }
        // LINKS //
        for(Map.Entry<Long, ArrayList<Long>> entry : dfdModel.getExternalStoragesForProcess().entrySet()) {
            for(Long index: entry.getValue()){
                lines.add(new Line(-1, Math.toIntExact(entry.getKey()), Math.toIntExact(index))); // создаём соединительную линию
            }
        }
        if(dfdModel.getProcesses().size() > 1) {
            Iterator itLeft = dfdModel.getProcesses().entrySet().iterator();
            Iterator itRight = dfdModel.getProcesses().entrySet().iterator();
            itRight.next();
            while (itRight.hasNext()) {
                Map.Entry<Long, String> meLeft = (Map.Entry<Long, String>) itLeft.next();
                Map.Entry<Long, String> meRight = (Map.Entry<Long, String>) itRight.next();
                lines.add(new Line(-1, Math.toIntExact(meLeft.getKey()), Math.toIntExact(meRight.getKey()))); // создаём соединительную линию
            }
        }
        // LINK INPUT, OUTPUT BLOCKS //
        lines.add(new Line(-1, indexInput, Math.toIntExact(dfdModel.getProcesses().keySet().stream().findFirst().get()))); // создаём соединительную линию
        lines.add(new Line(-1, Math.toIntExact(lastKeyProcesses), indexOutput)); // создаём соединительную линию

        return generateSpecialList(lines, cells);
    }

    private SpecialList generateSpecialList(ArrayList<Line> lines, HashMap<Integer, CellText> cells){ // конструируем специальный связный список(односвязный)
        SpecialList list = new SpecialList(); // лист

        HashMap<Integer, Cell> cellsCode = new HashMap<>(); // набор блоков input/output, process, temp-database
        for(Line l: lines){
            int ids[] = new int[]{l.getSource(), l.getTarget()};
            for(int id: ids){
                CellConstants typeSource = cells.get(id).getType(); // получаем тип создаваемого элемента
                // сначала узнаем, есть ли элемент в карте
                if(cellsCode.get(id) == null) { // если нет - добавляем
                    Cell c = CellCreator.CreateBlock(typeSource); // создаём элемент
                    String value = cells.get(id).getValue();
                    c.setData(value); // устанавливаем данные
                    cellsCode.put(id, c); // помещаем в карту созданный элемент
                }
            }
        }

        // теперь формируем TreeSet для линий(удаляя и переделывая нужные)
        TreeSet<Line> uniqueLines = new TreeSet<>(new LineComparator());
        for(int i = 0;i < lines.size();i++){
            Line l = lines.get(i);
            int idSource = l.getSource();
            int idTarget = l.getTarget();
            if(cells.get(idSource).getType() == CellConstants.CELL_TMP_DATABASE && cells.get(idTarget).getType() == CellConstants.CELL_PROCESS){ // если стрелка идёт от временных данных к процессу, то делаем реверс
                idSource += (idTarget - (idTarget = idSource));
            }
            Line line = new Line(l.getId(), idSource, idTarget);
            uniqueLines.add(line);
        }

        // получаем массив
        Line [] arrayUniqueLines = uniqueLines.toArray(new Line[uniqueLines.size()]);

        // ищем input
        ListElem node = null; // следующий узел
        int idCurrent = -1; // id найденного узла
        for(int i = 0;i<arrayUniqueLines.length;i++){
            int idSource = arrayUniqueLines[i].getSource(); // id источника
            if(cells.get(idSource).getType() == CellConstants.CELL_INPUT){ // нашли input
                int idTarget = arrayUniqueLines[i].getTarget(); // id принимающего
                idCurrent = idTarget;
                node = new ListElem(cellsCode.get(idTarget), null); // получили первый процесс
                ListElem elem = new ListElem(cellsCode.get(idSource), node); // получили начало списка
                list.setHead(elem); // установили начало списка
                break;
            }
        }

        // перебираем элементы далее:
        // к текущему процессу ищем все временные данные(хранилища)
        // следующий процесс. Как только находим процесс, переходим к нему, устанавливая найденные данные как предыдущие
        // TODO: попробовать удалять элементы, которые уже найдены
        int idTmp = -1; // временный id, указывает на след. элемент
        int tmpCounter = -1; // временная переменная, указывает на номер счётчика, чтобы совершить полный круг
        ListElem next = null; // следующий элемент списка
        boolean isEnd = false; // нашли конец, можно выйти
        for(int i = 0;i<arrayUniqueLines.length;i++){
            int idSource = arrayUniqueLines[i].getSource(); // id источника
            int idTarget = arrayUniqueLines[i].getTarget(); // id принимающего
            if(i == tmpCounter){ // если совершён полный круг по элементам - переходим к следующиему процессу
                idCurrent = idTmp; // меняем id процесса
                node = next; // заменяем ссылки для дальнейшего поиска
                tmpCounter = -1;
                if(isEnd){ // если метка выхода активна - выходим
                    break;
                }
            }
            if(idCurrent == idSource) { // если это нужный блок(есть связь с найденным)
                if (cells.get(idTarget).getType() == CellConstants.CELL_TMP_DATABASE) { // нашли временное хранилище
                    node.addTmpDataBlock(cellsCode.get(idTarget)); // добавили к элементу временное хранилище
                } else if (cells.get(idTarget).getType() == CellConstants.CELL_PROCESS) { // нашли процесс
                    idTmp = idTarget;
                    //tmpCounter = i;
                    next = new ListElem(cellsCode.get(idTarget), null); // получили следующий процесс процесс
                    node.setNext(next); // установили ссылку на следующий в текущий
                }else if(cells.get(idTarget).getType() == CellConstants.CELL_OUTPUT){ // нашли заключительный блок
                    ListElem endElem = new ListElem(cellsCode.get(idTarget), null); // получили заключительный блок выхода
                    node.setNext(endElem); // установили ссылку на заключительный блок
                    isEnd = true; // устанавливаем метку, что можно выйти
                }
                if(tmpCounter == -1){
                    tmpCounter = i;
                }
            }
            if(i == arrayUniqueLines.length-1){ // если цикл закончился - начинаем заново
                i = -1;
            }
        }

        return list; // возвращаем созданный лист
    }

    public Multimap<TypeFolders, GeneralGenerator> generationCode(SpecialList list){ // построение кода
        Multimap<TypeFolders, GeneralGenerator> classes = ArrayListMultimap.create();

        HashMap<TypeFolders, String> folders  = new HashMap<>(){{
            put(TypeFolders.MODELS, "models");
            put(TypeFolders.SERVICES, "services");
            put(TypeFolders.EXCEPTIONS, "exceptions");
        }};

        String nameEntity = null;
        InterfaceGenerator entityImpl = null;
        Method [] methods = null;
        Integer numProc = null;

        ListElem elem = list.getHead();
        while(elem != null){
            if(elem == list.getHead()){ // если это начало списка - то генерируем входную сущность, её интерфейс
                entityImpl = new InterfaceGenerator("public", "EntityImpl", "", "");
                entityImpl.addMethod(new Method("Object", "getData"));
                Method _m = new Method("void", "setData");
                _m.addArg(new MethodArg("Object", "data"));
                entityImpl.addMethod(_m);

                nameEntity = elem.getData().getValue();
                methods = new Method[]{
                        new Method("public", "void", "process", "_owner.process();\n// TODO: write your code here\n" +
                                "throw new ProcessException(\"Error method process in class \" + this.getClass().toString());\n" +
                                "// TODO: delete this line if you do not have exceptions in the method", "ProcessException", true),
                        new Method("public", nameEntity, "getEntity", "return _owner.getEntity();", true),
                        new Method("public", "void", "setEntity", " _owner.setEntity(ent);",
                                new MethodArg(nameEntity, "ent"), true),
                };
                ClassGenerator entity = new ClassGenerator("public", nameEntity, "EntityImpl", "");
                for(Method m: entityImpl.getMethods()){
                    Method current = (Method)m.cloneOverride();
                    current.setMethodCode("// TODO: write your code here\n");
                    if(!current.getType().equals("void")){
                        current.addCode("return null;");
                    }
                    entity.addMethod(current);
                }
                classes.put(TypeFolders.MODELS, entityImpl);
                classes.put(TypeFolders.MODELS, entity);
                classes = generateStartedEntities(classes, nameEntity);
            }else if(elem == list.getHead().getNext()){ // если это первый процесс
                Field decoratorComponentField = new Field("protected", "ComponentImpl", "_owner");
                classes = addProcess(classes, "DecoratorComponent", null, null, true,
                        new ArrayList<Field>(Arrays.asList(decoratorComponentField)),
                        new Method("public", "", "DecoratorComponent", "_owner = owner;", new MethodArg("ComponentImpl", "owner")),
                        "ComponentImpl", "", "", numProc);

                Method [] met = new Method[]{
                        new Method("public", "void", "process", "// TODO: write your code here\n" +
                                "throw new ProcessException(\"Error method process in class \" + this.getClass().toString());\n" +
                                "// TODO: delete this line if you do not have exceptions in the method", "ProcessException", true),
                        new Method("public", nameEntity, "getEntity", "return _entity;", true),
                        new Method("public", "void", "setEntity", "_entity = ent;",
                                new MethodArg(nameEntity, "ent"), true),
                };
                Field startProcessField = new Field("private", nameEntity, "_entity");
                numProc = new Integer(1);
                classes = addProcess(classes, elem.getData().getValue(),elem, met, false,
                        new ArrayList<Field>(Arrays.asList(startProcessField)),
                        new Method("public", "", elem.getData().getValue(), "_entity = ent;", new MethodArg(nameEntity, "ent")),
                        "ComponentImpl", "", "ComponentImpl thread = new " + elem.getData().getValue() + "(entity, ",
                        numProc++);
            }else if(elem.getNext() == null){ // если это последний блок
                ClassGenerator entityOut = new ClassGenerator("public", elem.getData().getValue(), "EntityImpl", "");
                entityOut.addMethod(new Method("public", "", elem.getData().getValue(), "// TODO: write your code here", new MethodArg(nameEntity, "ent")));
                for(Method m: entityImpl.getMethods()){
                    Method current = (Method)m.cloneOverride();
                    current.setMethodCode("// TODO: write your code here\n");
                    if(!current.getType().equals("void")){
                        current.addCode("return null;");
                    }
                    entityOut.addMethod(current);
                }
                classes.put(TypeFolders.MODELS, entityOut);
                workProcessStart.addCode("\ntry {\n" +
                        "thread.process();\n" +
                        "} catch (ProcessException e) {\n" +
                        "System.out.println(e.printMessage());\n" +
                        "return null;\n" +
                        "}\n\nreturn new " + elem.getData().getValue() + "(entity);");
                workProcessStart.setType(elem.getData().getValue());
                break;
            }else { // все остальные процессы, создаём процесс
                classes = addProcess(classes, elem.getData().getValue(),elem, methods, false,
                        null, new Method("public", "", elem.getData().getValue(), "super(owner);", new MethodArg("ComponentImpl", "owner")),
                        "", "DecoratorComponent", "thread = new " + elem.getData().getValue() + "(thread, ",
                        numProc++);
            }
            elem = elem.getNext();
        }

        // досоздаем управляющий класс
        workProcess = new ClassGenerator("public", "WorkProcess", "", "", workProcessStart);
        classes.put(TypeFolders.SERVICES, workProcess);

        // добавляем пакеты
        for(var code: classes.asMap().entrySet()){
            for(var c: code.getValue()) {
                c.setPackage(dfdModel.getPackageText() + "." + folders.get(code.getKey()));
                for(var f: folders.entrySet()) {
                    c.addImport(dfdModel.getPackageText() + "." + f.getValue() + ".*");
                }
            }
        }

        // test print code !!!
        for(var code: classes.asMap().entrySet()){
            System.out.println(code.getKey() + "\n");
            for(var c: code.getValue()) {
                System.out.println(c.getCode() + "\n");
            }
        }
        // end test !!!

        return classes;
    }

    private Multimap<TypeFolders, GeneralGenerator> generateStartedEntities(Multimap<TypeFolders, GeneralGenerator> classes, String entity){ // генерация базовых классов
        Field processExceptionFieldMessage = new Field("private", "String", "message"); // создаем исключение
        Method processExceptionContr = new Method("public", "", "ProcessException", "this.message = message;",
                new MethodArg("String", "message"));
        Method processExceptionPrint = new Method("public", "String", "printMessage", "return \"Error caught: \" + message;");
        ArrayList<Method> m = new ArrayList<>(Arrays.asList(processExceptionContr, processExceptionPrint));
        ClassGenerator processException = new ClassGenerator("public", "ProcessException", "", "Exception",
                m, processExceptionFieldMessage);
        classes.put(TypeFolders.EXCEPTIONS, processException);
        // Создаем главный интерфейс декоратора
        Method componentImplProcess = new Method("void", "process", "ProcessException");
        Method componentImplGetEntity = new Method(entity, "getEntity");
        Method componentImplSetEntity = new Method("void", "setEntity", new MethodArg(entity, "ent"));
        m = new ArrayList<>(Arrays.asList(componentImplProcess, componentImplGetEntity, componentImplSetEntity));
        componentImpl = new InterfaceGenerator("public", "ComponentImpl", "", "", m);
        classes.put(TypeFolders.MODELS, componentImpl);
        // Создаём управляющий класс
        workProcessStart = new Method("public", "", "start", "", new MethodArg(entity, "entity"));
        return classes;
    }

    private Multimap<TypeFolders, GeneralGenerator> addProcess(Multimap<TypeFolders, GeneralGenerator> classes, String name, ListElem elem, Method[] methods,
                                                         Boolean abstr, ArrayList<Field> fields, Method constr, String implem, String extend,
                                                         String defaultWorkLine, Integer numProc) { // генерация процесса
        ClassGenerator _class = null;
        if(fields == null) {
            _class = new ClassGenerator("public", name, implem, extend);
        } else{
            _class = new ClassGenerator("public", name, implem, extend, fields);
        }
        _class.setAbstract(abstr);
        if(numProc != null){
            _class.addField(new Field(true, true, "public", "int", "ITERATION_CALL", String.valueOf(numProc)));
        }
        StringBuilder workCode = new StringBuilder(defaultWorkLine);
        if(elem != null) {
            for (Cell tmpDb : elem.getTmpDataBlocks()) { // перебираем и добавляем все временные хранилища
                var db = classes.get(TypeFolders.MODELS).stream().filter(e -> e.getName().equals(tmpDb.getValue())).findFirst();
                if (db.isEmpty()) { // если хранилища нет - создаем и добавляем в главный метод
                    ClassGenerator clGen = new ClassGenerator("public", tmpDb.getValue(), "", ""); // создаём класс
                    classes.put(TypeFolders.MODELS, clGen); // добавляем к коллекции классов
                }

                StringBuilder nameArg = new StringBuilder(tmpDb.getValue()); // создаем имя аргумента для метода
                nameArg.setCharAt(0, Character.toLowerCase(nameArg.charAt(0)));
                MethodArg methodArg = new MethodArg(tmpDb.getValue(), nameArg.toString());

                boolean isFind = false; // default - элемента нет
                for(MethodArg ma: workProcessStart.getMethodArgs()){
                    if(ma.compareTo(methodArg) == 0){
                        isFind = true;
                        break;
                    }
                }
                if(!isFind) {
                    workProcessStart.addArg(new MethodArg(tmpDb.getValue(), nameArg.toString()));
                }

                workCode.append(nameArg.toString() + ", ");
                constr.addArg(new MethodArg(tmpDb.getValue(), nameArg.toString()));
                constr.addCode("this." + nameArg.toString() + " = " + nameArg.toString() + ";");
                _class.addField(new Field("private", tmpDb.getValue(), nameArg.toString()));
            }
        }
        if(!defaultWorkLine.equals("")) {
            workCode.delete(workCode.length() - 2, workCode.length());
            workProcessStart.addCode(workCode.toString() + ");\n");
        }
        _class.addMethod(constr);
        if(methods != null) {
            for (Method m : methods) {
                _class.addMethod((Method) m.clone());
            }
        }
        classes.put(TypeFolders.SERVICES, _class);
        return classes;
    }
}
