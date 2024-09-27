class Horses {

  public static void move() {
    System.out.println("Лошадь ходит");
  }

  public static void eat(String meal) {
    if (meal.equals("Трава")) {
      System.out.println("Лошадь ест");
    } else {
      System.out.println("Лошадь это не ест");
    }
  }
}

class Tigers {

  public static void move() {
    System.out.println("Тигр ходит");
  }

  public static void eat(String meal) {
    if (meal.equals("Говядина")) {
      System.out.println("Тигр ест");
    } else {
      System.out.println("Тигр это не ест");
    }
  }
}

class Dolphines {

  public static void swim() {
    System.out.println("Дельфин плавает");
  }

  public static void eat(String meal) {
    if (meal.equals("Рыба")) {
      System.out.println("Дельфин ест");
    } else {
      System.out.println("Дельфин это не ест");
    }
  }
}

class Camels {

  public static void move() {
    System.out.println("Верблюд ходит");
  }

  public static void eat(String meal) {
    if (meal.equals("Трава")) {
      System.out.println("Верблюд ест");
    } else {
      System.out.println("Верблюд это не ест");
    }
  }
}

class Eagles {

  public static void fly() {
    System.out.println("Орел летает");
  }

  public static void eat(String meal) {
    if (meal.equals("Мясо")) {
      System.out.println("Орел ест");
    } else {
      System.out.println("Орел это не ест");
    }
  }
}

public class Main {

  public static void main(String[] args) {
    Tigers tiger = new Tigers();
    tiger.move();
    Eagles eagle = new Eagles();
    eagle.eat("Мясо");
    eagle.fly();
    Horses horse = new Horses();
    horse.eat("Трава");
    Camels camel = new Camels();
    camel.move();
    Dolphines dolphine = new Dolphines();
    dolphine.eat("Говядина");
    dolphine.swim();
  }
}
