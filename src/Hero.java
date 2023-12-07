import java.util.Scanner;

final class Hero {
    String name;
    int strength;
    int dexterity;
    int vitality;
    int energy;
    int health;
    int damage;
    int points; // Точки за подобрения
    int defaultDamage;
    int defaultHealth;

    public Hero(String heroName) {
        name = heroName;
        strength = 5;
        dexterity = 5;
        vitality = 5;
        energy = 5;
        points = 0;
        defaultHealth = 100;
        defaultDamage = 10;
        updateStats();
    }

    public void attack(Hero enemy) {
        if (!enemy.tryBlockAttack()) {
            enemy.takeDamage(damage);
            System.out.println(name + " нанесе " + damage + " щети на " + enemy.name);
        } else {
            System.out.println(enemy.name + " блокира атаката на " + name);
        }
    }

    public boolean tryBlockAttack() {
        double blockChance = dexterity * 0.002;
        return Math.random() < blockChance;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;
    }

    public void updateStats() {
        damage = this.defaultDamage + strength * 2;
        health = this.defaultHealth + vitality * 10;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void printStats() {
        System.out.println("Герой: " + name);
        System.out.println("Сила: " + strength);
        System.out.println("Ловкост: " + dexterity);
        System.out.println("Живот: " + vitality);
        System.out.println("Енергия: " + energy);
        System.out.println("Точки за подобрения: " + points);
        System.out.println("Здраве: " + health);
    }

    public String serialize() {
        return name + "," + strength + "," + dexterity + "," + vitality + "," + energy + "," + points + "," + defaultDamage + "," + defaultHealth;
    }

    public void deserialize(String serializedHero) {
        String[] heroAttributes = serializedHero.split(",");
        name = heroAttributes[0];
        strength = Integer.parseInt(heroAttributes[1]);
        dexterity = Integer.parseInt(heroAttributes[2]);
        vitality = Integer.parseInt(heroAttributes[3]);
        energy = Integer.parseInt(heroAttributes[4]);
        points = Integer.parseInt(heroAttributes[5]);
        defaultDamage = Integer.parseInt(heroAttributes[6]);
        defaultHealth = Integer.parseInt(heroAttributes[7]);
        updateStats();
    }

    public void improveAttribute(String attribute) {
        if (points <= 0) {
            System.out.println("Нямате достатъчно точки.");
            return;
        }

        switch (attribute) {
            case "strength":
                strength++;
                break;
            case "dexterity":
                dexterity++;
                break;
            case "vitality":
                vitality++;
                break;
            case "energy":
                energy++;
                break;
        }
        updateStats();
        points--;
    }
}
