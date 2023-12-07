class BattleGame implements IGame {
    private Hero hero;
    private final GameServer gameServer;

    public BattleGame(Hero hero, GameServer gameServer) {
        this.hero = hero;
        this.gameServer = gameServer;
    }

    public void fight(Hero hero2) {
        while (hero.isAlive() && hero2.isAlive()) {
            hero.attack(hero2);
            if (hero2.isAlive()) {
                hero2.attack(hero);
            }
        }

        Hero winner = hero.isAlive() ? hero : hero2;
        if (winner == hero) {
            gameServer.save();
        }
        System.out.println("Победителят е " + winner.name);
    }

    @Override
    public void start() {
        System.out.println("Започва битка");

    }

    @Override
    public Hero getHero() {
        return hero;
    }
}
