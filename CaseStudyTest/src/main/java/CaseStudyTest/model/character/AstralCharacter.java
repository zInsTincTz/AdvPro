package CaseStudyTest.model.character;

public class AstralCharacter extends BasedCharacter {
    public AstralCharacter(String name,String imgpath,int basedDef,int basedRes){
        this.name = name;
        this.type = DamageType.astral;
        this.imgpath = imgpath;
        this.fullHp = 40;
        this.basedPow = 40;
        this.basedDef = basedDef;
        this.basedRes = basedRes;
        this.hp = this.fullHp;
        this.power = this.basedPow;
        this.defense = basedDef;
        this.resistance = basedRes;
    }
}
