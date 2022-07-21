package CaseStudyTest.controller;


import CaseStudyTest.model.character.DamageType;
import CaseStudyTest.model.item.Armor;
import CaseStudyTest.model.item.BasedEquipment;
import CaseStudyTest.model.item.Weapon;

import java.util.ArrayList;

public class GenItemList {
    public static ArrayList<BasedEquipment> setUpItemList() {
        ArrayList<BasedEquipment> itemLists = new ArrayList<BasedEquipment>(7);
        itemLists.add(new Weapon("Sword",10,DamageType.physical,"assets/sword1.png"));
        itemLists.add(new Weapon("Gun",20,DamageType.physical,"assets/gun1.png"));
        itemLists.add(new Weapon("Staff",30,DamageType.magical,"assets/staff1.png"));
        itemLists.add(new Weapon("Grimoire",30,DamageType.magical,"assets/grimoire1.png"));
        itemLists.add(new Weapon("Orb",40,DamageType.astral,"assets/orb1.png"));
        itemLists.add(new Armor("shirt",20,50,"assets/shirt1.png"));
        itemLists.add(new Armor("armor",50,20,"assets/armor1.png"));
        itemLists.add(new Armor("robe",35,35,"assets/robe1.png"));
        return itemLists;
    }
}