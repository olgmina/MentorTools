const multipleChoiceTemplate: string = 
`::Название::Вопрос {
    =Правильный вариант
    ~Вариант 2
    ~Вариант 3
    ~Вариант 4
}`;
const multipleChoiceAnswersTemplate: string = 
`::Название::Вопрос {
    ~%-100%Вариант 1
    ~%50%Вариант 2
    ~%50%Вариант 3
    ~%-100%Вариант 4
}`;
const trueFalseTemplate: string = `::Название::Утверждение.{TRUE или FALSE}`;
const shortAnswerTemplate: string = `::Название::Вопрос {=ответ =ответ в другом виде}`;
const matchingTemplate: string = 
`::Название::Вопрос {
    =Вариант 1 -> Вариант а
    =Вариант 2 -> Вариант б
    =Вариант 3 -> Вариант в
    =Вариант 4 -> Вариант г
}`;
const missingWordTemplate: string = `::Название::Начало предложения {=правильный ответ ~неправильный ответ} конец предложения.`;
const numericalTemplate: string = `::Название::Вопрос {#ответ числом:возможная погрешность}`;
const essayTemplate: string = `::Название::Вопрос {}`;

export const templateFactory = (type: string): string => {
    switch (type) {
        case "Множественный выбор":
            return multipleChoiceTemplate;
        case "Множественный выбор с множественными ответами":
            return multipleChoiceAnswersTemplate;
        case "Правда-Ложь":
            return trueFalseTemplate;
        case "Короткий ответ":
            return shortAnswerTemplate;
        case "Сопоставление":
            return matchingTemplate;
        case "Пропущенное слово":
            return missingWordTemplate;
        case "Числовой ответ":
            return numericalTemplate;
        case "Эссе":
            return essayTemplate;
        default:
            return '';
    }
}