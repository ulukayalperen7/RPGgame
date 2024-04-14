import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class HW05_20220808006 {
    public static void main(String[] args) {

    }
}

interface Damageable {

    public void takeDamage(int damage);

    public void takeHealing(int healing);

    public boolean isAlive();
}

interface Caster {

    public void castSpell(Damageable target);

    public void learnSpell(Spell spell);
}

interface Combat extends Damageable {

    public void attack(Damageable target);

    public void lootWeapon(Weapon weapon);
}

interface Usable {

    public int use();
}

class Spell implements Usable {

    private int minHeal;
    private int maxHeal;
    private String name;

    public Spell(String name, int minHeal, int maxHeal) {
        setName(name);
        this.minHeal = minHeal;
        this.maxHeal = maxHeal;
    }

    @Override
    public int use() {
        return heal();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int heal() {
        Random rand = new Random();
        int random = rand.nextInt(minHeal, maxHeal + 1);
        return random;
    }

}

class Weapon implements Usable {

    private int minDamage;
    private int maxDamage;
    private String name;

    public Weapon(String name, int minDamage, int maxDamage) {
        setName(name);
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int attack() {
        Random rand = new Random();
        int random = rand.nextInt(minDamage, maxDamage + 1);
        return random;
    }

    @Override
    public int use() {
        return attack();
    }

}

class Attributes {

    private int strength;
    private int intelligence;

    public Attributes() {
        this.strength = 3;
        this.intelligence = 3;
    }

    public Attributes(int strength, int intelligence) {
        this.strength = strength;
        this.intelligence = intelligence;
    }

    public void increaseStrength(int amount) {
        strength += amount;
    }

    public void increaseIntelligence(int amount) {
        intelligence += amount;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getStrength() {
        return strength;
    }

    @Override
    public String toString() {
        return "Attributes [Strength= " + strength + ", intelligence= " + intelligence + "]";
    }
}

abstract class Character implements Comparable<Character>, Damageable {

    private String name;
    protected int level;
    protected Attributes attributes;
    protected int health;

    public Character(String name, Attributes attributes) {
        this.name = name;
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    abstract public void levelUp();

    @Override
    public String toString() {
        return getName() + "Lvl: " + level + " - " + attributes;
    }
}

abstract class PlayableCharacter extends Character {

    private boolean inParty;
    private Party party;

    public PlayableCharacter(String name, Attributes attributes) {
        super(name, attributes);
    }

    public boolean isInParty() {
        return inParty;
    }

    @Override
    public void levelUp() {
        level++;
    }

    public void joinParty(Party party) throws AlreadyInPartyException {

        try {
            if (isInParty()) {
                throw new AlreadyInPartyException("this character is already in the party");
            } else {
                party.addCharacter(this);
                inParty = true;
                this.party = party;
            }
        } catch (PartyLimitReachedException e) {
            System.out.println(e.getMessage());
        }
    }

    public void quitParty() throws CharacterIsNotInPartyException {

        if (isInParty()) {
            try {
                party.removeCharacter(this);
                inParty = false;
                this.party = null;
            } catch (CharacterIsNotInPartyException e) {
                System.out.println(e.getMessage());
            }
        } else {
            throw new CharacterIsNotInPartyException("character is not in the party!");
        }
    }
}

abstract class NonPlayableCharacter extends Character {
    public NonPlayableCharacter(String name, Attributes attributes) {
        super(name, attributes);
    }
}

class Merchant extends NonPlayableCharacter {

    public ArrayList<Usable> inventory;

    public Merchant(String name) {
        super(name, new Attributes(0, 0));
        this.inventory = new ArrayList<>();
    }

    @Override
    public void takeDamage(int damage) {
        health -= damage;
    }

    @Override
    public void takeHealing(int healing) {
        health += healing;
    }

    @Override
    public int compareTo(Character o) {
        return 0;
    }

    @Override
    public boolean isAlive() {
        if (health > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void levelUp() {

    }

    public void display() {
        for (int i = 0; i < inventory.size(); i++) {
            System.out.println(i);
        }
    }

    public Usable buy(int itemNumber) throws ItemNotFoundException {
        try {
            return inventory.get(itemNumber);
        } catch (IndexOutOfBoundsException e) {
            throw new ItemNotFoundException("item not found!");
        }
    }

    public void sell(Usable item) {
        inventory.add(item);
    }
}

class Skeleton extends NonPlayableCharacter implements Combat {

    public Skeleton(String name, Attributes attributes) {
        super(name, attributes);
    }

    @Override
    public void lootWeapon(Weapon weapon) {

    }

    @Override
    public void levelUp() {
        level++;
        attributes.increaseIntelligence(1);
        attributes.increaseStrength(1);
    }

    @Override
    public void takeHealing(int healing) {
        takeDamage(healing);
    }

    @Override
    public int getLevel() {
        return super.getLevel();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void attack(Damageable target) {
        target.takeDamage(attributes.getIntelligence() + attributes.getStrength());
    }

    @Override
    public boolean isAlive() {
        if (health > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void takeDamage(int damage) {
        health -= damage;
    }

    @Override
    public int compareTo(Character o) {
        return 0;
    }

}

class Warrior extends PlayableCharacter implements Combat {

    private Usable weapon;

    public Warrior(String name) {
        super(name, new Attributes(4, 2));
        health = 35;
    }

    @Override
    public int getLevel() {
        return super.getLevel();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void attack(Damageable target) {
        target.takeDamage(attributes.getStrength() + weapon.use());
    }

    @Override
    public void lootWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    @Override
    public boolean isAlive() {
        if (health > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void takeDamage(int damage) {
        health -= damage;
    }

    @Override
    public void takeHealing(int healing) {
        health += healing;
    }

    @Override
    public int compareTo(Character o) {
        return 0;
    }

    @Override
    public void levelUp() {
        attributes.increaseIntelligence(1);
        attributes.increaseStrength(2);
    }

}

class Cleric extends PlayableCharacter implements Caster {

    private Usable spell;

    public Cleric(String name) {
        super(name, new Attributes(2, 4));
        health = 25;
    }

    @Override
    public void levelUp() {
        attributes.increaseIntelligence(2);
        attributes.increaseStrength(1);
    }

    @Override
    public int getLevel() {
        return super.getLevel();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void castSpell(Damageable target) {
        target.takeHealing(spell.use() + attributes.getIntelligence());
    }

    @Override
    public void learnSpell(Spell spell) {
        this.spell = spell;
    }

    @Override
    public int compareTo(Character o) {
        return 0;
    }

    @Override
    public boolean isAlive() {
        if (health > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void takeDamage(int damage) {
        health -= damage;
    }

    @Override
    public void takeHealing(int healing) {
        health += healing;
    }

}

class Paladin extends PlayableCharacter implements Caster, Combat {

    private Usable weapon;
    private Usable spell;

    public Paladin(String name) {
        super(name, new Attributes());
        health = 30;
    }

    @Override
    public void levelUp() {
        if (level % 2 == 0) {
            attributes.increaseIntelligence(1);
            attributes.increaseStrength(2);
        } else {
            attributes.increaseIntelligence(2);
            attributes.increaseStrength(1);
        }
    }

    @Override
    public int getLevel() {
        return super.getLevel();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void castSpell(Damageable target) {
        target.takeHealing(spell.use() + attributes.getIntelligence());
    }

    @Override
    public void learnSpell(Spell spell) {
        this.spell = spell;
    }

    @Override
    public void attack(Damageable target) {
        target.takeDamage(attributes.getStrength() + weapon.use());
    }

    @Override
    public void lootWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    @Override
    public boolean isAlive() {
        if (health > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void takeDamage(int damage) {
        health -= damage;
    }

    @Override
    public void takeHealing(int healing) {
        health += healing;
    }

    @Override
    public int compareTo(Character o) {
        return 0;
    }

}

class Party {

    private final int partyLimit = 8;
    private ArrayList<Character> fighters;// savaşcılar COMBAT
    private ArrayList<Character> healers; // sifacılar CASTER
    private int mixedCount; // şovalye sayısı

    public Party() {
        fighters = new ArrayList<>();
        healers = new ArrayList<>();
    }

    public void addCharacter(PlayableCharacter character) throws PartyLimitReachedException {

        if (fighters.size() + healers.size() >= partyLimit) {
            throw new PartyLimitReachedException("the party has already reached to the limit!");
        } else {
            if (character instanceof Combat) {
                fighters.add(character);
                if (character instanceof Paladin) {
                    mixedCount++;
                }
            } else if (character instanceof Caster) {
                healers.add(character);
            }
        }
    }

    public void removeCharacter(PlayableCharacter character) throws CharacterIsNotInPartyException {

        if (character instanceof Combat) {
            fighters.remove(character);
            if (character instanceof Paladin) {
                mixedCount++;
            }
        } else if (character instanceof Caster) {
            healers.remove((character));
        } else {
            throw new CharacterIsNotInPartyException("character isn't in the party!");
        }
    }

    public void partyLevelUp() {
        for (int i = 0; i < fighters.size(); i++) {
            fighters.get(i).level++;
        }
        for (int i = 0; i < healers.size(); i++) {
            healers.get(i).level++;
        }
    }

    @Override
    public String toString() {
        ArrayList<Character> newArray = new ArrayList<>();
        newArray.addAll(fighters);
        newArray.addAll(healers);

        Collections.sort(newArray, new Comparator<Character>() {
            @Override
            public int compare(Character char1, Character char2) {
                return Integer.compare(char1.getLevel(), char2.getLevel());
            }
        });

        StringBuilder sb = new StringBuilder();
        for (Character character : newArray) {
            sb.append(character).append("\n");
        }

        return sb.toString();
    }
}

class PartyLimitReachedException extends Exception {

    public PartyLimitReachedException(String text) {
        super(text);
    }
}

class AlreadyInPartyException extends Exception {
    public AlreadyInPartyException(String text) {
        super(text);
    }
}

class CharacterIsNotInPartyException extends Exception {
    public CharacterIsNotInPartyException(String text) {
        super(text);
    }
}

class ItemNotFoundException extends Exception {
    public ItemNotFoundException(String text) {
        super(text);
    }
}
