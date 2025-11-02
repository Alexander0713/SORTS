import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.Comparator;

public class AviaSoulsTest {

    @Test
    public void testTicketCompareTo() {
        Ticket ticket1 = new Ticket("MOW", "LED", 5000, 1200, 1400);
        Ticket ticket2 = new Ticket("MOW", "LED", 7000, 1300, 1500);
        Ticket ticket3 = new Ticket("MOW", "LED", 5000, 1400, 1600);

        // ticket1 дешевле ticket2
        Assertions.assertTrue(ticket1.compareTo(ticket2) < 0);

        // ticket2 дороже ticket1
        Assertions.assertTrue(ticket2.compareTo(ticket1) > 0);

        // ticket1 и ticket3 одинаковой цены
        Assertions.assertEquals(0, ticket1.compareTo(ticket3));
    }

    @Test
    public void testTicketTimeComparator() {
        Ticket ticket1 = new Ticket("MOW", "LED", 5000, 1200, 1400); // 200 минут
        Ticket ticket2 = new Ticket("MOW", "LED", 7000, 1300, 1500); // 200 минут
        Ticket ticket3 = new Ticket("MOW", "LED", 6000, 1000, 1300); // 300 минут

        TicketTimeComparator comparator = new TicketTimeComparator();

        // ticket1 и ticket2 одинаковое время полета
        Assertions.assertEquals(0, comparator.compare(ticket1, ticket2));

        // ticket1 быстрее ticket3
        Assertions.assertTrue(comparator.compare(ticket1, ticket3) < 0);

        // ticket3 медленнее ticket1
        Assertions.assertTrue(comparator.compare(ticket3, ticket1) > 0);
    }

    @Test
    public void testSearchSortedByPrice() {
        AviaSouls souls = new AviaSouls();

        Ticket ticket1 = new Ticket("MOW", "LED", 5000, 1200, 1400);
        Ticket ticket2 = new Ticket("MOW", "LED", 3000, 1300, 1500);
        Ticket ticket3 = new Ticket("MOW", "LED", 7000, 1400, 1600);
        Ticket ticket4 = new Ticket("MOW", "KZN", 4000, 1500, 1700); // другой маршрут

        souls.add(ticket1);
        souls.add(ticket2);
        souls.add(ticket3);
        souls.add(ticket4);

        Ticket[] result = souls.search("MOW", "LED");

        // Проверяем количество найденных билетов
        Assertions.assertEquals(3, result.length);

        // Проверяем сортировку по цене (возрастание) с помощью assertArrayEquals
        Ticket[] expected = {ticket2, ticket1, ticket3};
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    public void testSearchAndSortByTime() {
        AviaSouls souls = new AviaSouls();

        Ticket ticket1 = new Ticket("MOW", "LED", 5000, 1200, 1400); // 200 минут
        Ticket ticket2 = new Ticket("MOW", "LED", 3000, 1000, 1330); // 330 минут
        Ticket ticket3 = new Ticket("MOW", "LED", 7000, 900, 1000);  // 100 минут
        Ticket ticket4 = new Ticket("MOW", "KZN", 4000, 1500, 1700); // другой маршрут

        souls.add(ticket1);
        souls.add(ticket2);
        souls.add(ticket3);
        souls.add(ticket4);

        TicketTimeComparator timeComparator = new TicketTimeComparator();
        Ticket[] result = souls.searchAndSortBy("MOW", "LED", timeComparator);

        // Проверяем количество найденных билетов
        Assertions.assertEquals(3, result.length);

        // Проверяем сортировку по времени полета (возрастание) с помощью assertArrayEquals
        Ticket[] expected = {ticket3, ticket1, ticket2};
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    public void testSearchNoResults() {
        AviaSouls souls = new AviaSouls();

        Ticket ticket1 = new Ticket("MOW", "LED", 5000, 1200, 1400);
        Ticket ticket2 = new Ticket("LED", "MOW", 3000, 1300, 1500);

        souls.add(ticket1);
        souls.add(ticket2);

        Ticket[] result = souls.search("MOW", "KZN");

        // Проверяем, что нет результатов для несуществующего маршрута с помощью assertArrayEquals
        Ticket[] expected = {};
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    public void testSearchEmptyRepository() {
        AviaSouls souls = new AviaSouls();

        Ticket[] result = souls.search("MOW", "LED");

        // Проверяем поиск в пустом репозитории с помощью assertArrayEquals
        Ticket[] expected = {};
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    public void testSearchSingleTicket() {
        AviaSouls souls = new AviaSouls();

        Ticket ticket = new Ticket("MOW", "LED", 5000, 1200, 1400);
        souls.add(ticket);

        Ticket[] result = souls.search("MOW", "LED");

        // Проверяем поиск одного билета с помощью assertArrayEquals
        Ticket[] expected = {ticket};
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    public void testSearchWithDifferentComparators() {
        AviaSouls souls = new AviaSouls();

        Ticket ticket1 = new Ticket("MOW", "LED", 5000, 1200, 1400); // цена 5000, время 200
        Ticket ticket2 = new Ticket("MOW", "LED", 3000, 1000, 1330); // цена 3000, время 330
        Ticket ticket3 = new Ticket("MOW", "LED", 7000, 900, 1000);  // цена 7000, время 100

        souls.add(ticket1);
        souls.add(ticket2);
        souls.add(ticket3);

        // Сортировка по цене
        Ticket[] byPrice = souls.search("MOW", "LED");
        Ticket[] expectedByPrice = {ticket2, ticket1, ticket3};
        Assertions.assertArrayEquals(expectedByPrice, byPrice);

        // Сортировка по времени
        TicketTimeComparator timeComparator = new TicketTimeComparator();
        Ticket[] byTime = souls.searchAndSortBy("MOW", "LED", timeComparator);
        Ticket[] expectedByTime = {ticket3, ticket1, ticket2};
        Assertions.assertArrayEquals(expectedByTime, byTime);
    }
}