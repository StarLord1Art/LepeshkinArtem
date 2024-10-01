interface Flying {

  void fly();
}

interface Terrestrial {

  void walk();
}

interface Waterfowl {

  void swim();
}

interface BaseMeal {

  public String getMealName();
}

class Meat implements BaseMeal {

  public String getMealName() {
    return "мясо";
  }
}

class Beef extends Meat {

  public String getMealName() {
    return "говядину";
  }
}

class Fish extends Meat {

  public String getMealName() {
    return "рыбу";
  }
}

class Grass implements BaseMeal {

  public String getMealName() {
    return "траву";
  }
}

abstract class BaseType {

  public String animalTitle;

  void eat(BaseMeal meal) {
    System.out.println(animalTitle + " ест " + meal.getMealName());
  }
}

abstract class Herbivores extends BaseType {

  void eat(Grass meal) {
    super.eat(meal);
  }
}

abstract class Predators extends BaseType {

  void eat(Meat meal) {
    super.eat(meal);
  }
}

class Camel extends Herbivores implements Terrestrial {

  public Camel() {
    animalTitle = "Верблюд";
  }

  public void walk() {
    System.out.println(animalTitle + " ходит");
  }

  void eat(Grass meal) {
    super.eat(meal);
  }
}

class Dolphin extends Predators implements Waterfowl {

  public Dolphin() {
    animalTitle = "Дельфин";
  }

  public void swim() {
    System.out.println(animalTitle + " плавает");
  }

  void eat(Fish meal) {
    super.eat(meal);
  }
}

class Eagle extends Predators implements Flying {

  public Eagle() {
    animalTitle = "Орел";
  }

  public void fly() {
    System.out.println(animalTitle + " летает");
  }

  void eat(Meat meal) {
    super.eat(meal);
  }
}

class Hourse extends Herbivores implements Terrestrial {

  public Hourse() {
    animalTitle = "Лощадь";
  }

  public void walk() {
    System.out.println(animalTitle + " ходит");
  }

  void eat(Grass meal) {
    super.eat(meal);
  }
}

class Tiger extends Predators implements Terrestrial {

  public Tiger() {
    animalTitle = "Тигр";
  }

  public void walk() {
    System.out.println(animalTitle + " ходит");
  }

  void eat(Beef meal) {
    super.eat(meal);
  }
}

public class Main {

  public static void main(String[] args) {
    Camel camel = new Camel();
    Grass grass = new Grass();
    camel.eat(grass);
    Dolphin dolphin = new Dolphin();
    dolphin.swim();
    Eagle eagle = new Eagle();
    eagle.fly();
    Hourse hourse = new Hourse();
    hourse.eat(grass);
    Tiger tiger = new Tiger();
    tiger.walk();
  }
}
