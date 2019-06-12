 Git:
1. Создать Git проект jd2_project_week3
2. Проинициализировать проект 
3. Создать ветку develop
4. Написать проект
5. Внести изменения в ветку develop c соответствующим комментарием
6. Сделать pull request в ветку master
7. Прислать ссылку на pull request

Проект:
	Требования:
	- Spring Boot 2.1.4
	- JDBC
	- MySQL
1. Создать многомодульный проект, состоящий из трех jar подпроектов: spring-boot-module/service-module/data-module
2. Создать зависимость service-module oт data-module
3. Создать зависимость spring-boot-module oт service-module
4. Создать и реализовать интерфейсы в модуле service-module  
public interface ItemService {
    ItemDTO add(ItemDTO item);
    List<ItemDTO> getItems();
    int update(Long id, String status);
}
public interface AuditItemService {
    AuditItem save(AuditItem item);
}
5. Создать и реализовать необходимые репозитории в модуле data-module
6. Требование к модели в базе данных:
	- id
	- name(не более 40 символов)
	- status(READY, COMPLETED)
7. Конвертация в сущность в базе на уровне service-module
8. Работа с транзакциями на уровне service-module
9. Создать контроллер, который имеет методы: 
	- показывает все Item
	- добавляет Item
	- обновляет статус айтема по id. 
10. Написать на уровне service-module Advice, который создает, на каждое изменение Item, AuditItem с полями:
	- id
	- action(CREATE, UPDATED) 
	- item_id
	- date
12. Покрыть unit/integration тестами нужные компонент
