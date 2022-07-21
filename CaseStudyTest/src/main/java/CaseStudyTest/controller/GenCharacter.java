package CaseStudyTest.controller;

import CaseStudyTest.model.character.AstralCharacter;
import CaseStudyTest.model.character.BasedCharacter;
import CaseStudyTest.model.character.MagicalCharacter;
import CaseStudyTest.model.character.PhysicalCharacter;

import java.util.Random;

public class GenCharacter {
    public static BasedCharacter setUpCharacter() {
        BasedCharacter character;
        Random rand = new Random();
        int type = rand.nextInt(3)+1;
        int basedDef = rand.nextInt(50)+1;
        int basedRes = rand.nextInt(50)+1;

        if (type == 1) {
            character = new MagicalCharacter("Magic Caster","assets/wizard.png", basedDef,basedRes);
        }
        else if (type == 2) {
            character = new PhysicalCharacter("Knight","assets/knight.png", basedDef,basedRes);
        }
        else {
            character = new AstralCharacter("Battle Mage", "assets/Battlemage.png", basedRes,basedRes);
        }
        return character;
    }
}