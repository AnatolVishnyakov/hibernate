**Конфигурирование**

1. Настройка hibernate.cfg.xml
2. Настройка hibernate.properties
3. Передача параметров приложению через VM Options, например:
`-Dhibernate.connection.url=jdbc:postgresql://localhost:5432/hibernate`
4. Создание конфига непосредственно в коде

**Маппинг**

1. Через xml.
2. Через аннотации.

**Аннотация Basic**

Не обязательная аннотация, имеет два параметра:
- fetchtype - способ получения значения (Lazy - ленивая (по требованию), Eager - всегда достает значение)
- optional - обязательность (true/false)

**Аннотация Column**

Позволяет производить с колонкой некоторые изменения:
- имя колонки;
- уникальность;
- возможность сохранять пустое значение;
- возможность вставки значение (insertable);
- возможность обновления значений (updatable);
- детальная настройка колонки через прямой sql (columnDefinition);
- ссылка на другую таблицу в которой хранится поле;
- длина поля;

и т.д.

**Аннотация Type**

Указание типа значения, которое хранится в колонке.

**Аннотация Enumerated**

Маппирует enum элементы. По умолчанию хранит порядковвый номер enum элемента.

**Аннотация Temporal. Маппинг даты**

С помощью аннотации Temporal возможно хранение только времени/даты/ДатаВремя.

**Автоматическая генерация даты**

- CreationTimestamp (при создании значения)
- UpdateTimestamp (при каждом обновлении)

**Получение данных из БД**

- load (при запросе не существующей сущности вылетит ошибка и использует прокси объект)
- get (при запросе не существующего сущности ошибка НЕ вылетит)

**Аннотация Formula**

Составное поле.
Аннотацию @Formula используют для указания фрагмента SQL, который Hibernate будет выполнять при получении объекта из базы данных. 
Возвращаемое значение мапится на атрибут сущности, которое доступно только для чтения.

**Embeddable объект**

Встраиваемый объект. 
Сущность сохранится в туже таблицу, в которой является полем.
Не нужно мапить, не нужно создавать отдельную entity.

**Имя таблицы**

- Указать Entity(name = "...")
- Использовать аннотацию Table(name = "...")

**Access type**

Аннотации вешаются либо на поля (рефлексия), либо на геттеры/сеттеры.
Возможно отдельно настраивать через аннотацию Access(AccessType.FIELD), можно помечать отдельно на класс.

**Аннотация Transient**

Если не хотим сохранять поле в БД, то помечаем аннотацией Transient или ключевым словом transient.

**Аннотация Id**

Обязателен с hibernate 5.
Каждая Entity должна иметь минимум одно поля Id.
Генератор id можно задать через аннотацию GeneratedValue с указанием стратегии создания идентификатора.

**Аннотация EmbeddedId (составной Id)**

Класс помечается как Embeddable, должен реализовывать Serializable, equals, hashcode, default constructor.
Затем поле сущности помечается аннотацией EmbeddedId. 

**Отношение между классами**
-
1. OneToOne (можно указывать в какой сущности создавать колонку через mappedBy, Cascade.PERSIST, ...)
2.

**Проблема N+1**

При получении сущности, которая содержит внутри себя связанные сущности, может возникнуть проблема N + 1.
Можно решить при помощи аннотации @LazyToOne(LazyToOneOption.NO_PROXY). 