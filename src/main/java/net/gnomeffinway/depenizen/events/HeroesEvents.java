/*    */ package net.gnomeffinway.depenizen.events;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;

/*    */ import net.aufdemrand.denizen.events.EventManager;
/*    */ import net.aufdemrand.denizen.objects.Element;
/*    */ import net.aufdemrand.denizen.objects.dEntity;
/*    */ import net.aufdemrand.denizen.objects.dNPC;
/*    */ import net.aufdemrand.denizen.objects.dObject;
/*    */ import net.gnomeffinway.depenizen.Depenizen;

/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.Cancellable;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;

/*    */ import com.herocraftonline.heroes.api.events.ClassChangeEvent;
/*    */ import com.herocraftonline.heroes.api.events.HeroChangeLevelEvent;
/*    */ import com.herocraftonline.heroes.api.events.HeroEnterCombatEvent;
/*    */ import com.herocraftonline.heroes.api.events.SkillUseEvent;
/*    */ 
/*    */
/*    */ 
/*    */ 
/*    */ public class HeroesEvents implements Listener {
/*    */  
/*    */   
/*    */   public HeroesEvents(Depenizen depenizen) {
/*    */   
/* 21 */   depenizen.getServer().getPluginManager().registerEvents(this, depenizen);
/*    */   
/*    */	}
/*    */
/*    */
/*    */
/*
          Event :  on hero changes class: / on hero changes class to <class>: 
          Context: - <context.class> Returns the class the hero changed to.
                   - <context.cost>  Returns the price of an class change.
                   - <context.entity> Returns the name of the Player who changed his class */
/*    *(
/*    */
/*    */   @EventHandler
/*    */   public void changeClass(ClassChangeEvent event) {
/*    */   
/* 38 */     dEntity hero = new dEntity(event.getHero().getEntity());
/* 39 */     Player player = null;
/* 40 */     dNPC npc = null;
/*    */ 
/* 42 */     Map<String, dObject> context = new HashMap<String, dObject>();
/* 43 */     context.put("class", new Element(event.getTo().getName()));
/* 44 */     context.put("cost", new Element(Double.valueOf(event.getCost())));
/* 45 */     context.put("entity", hero);
/*    */ 
/* 47 */     if (hero.isNPC())
/* 48 */       npc = hero.getDenizenNPC();
/* 49 */     else if (hero.isPlayer()) {
/* 50 */       player = hero.getPlayer();
/*    */	}
/*    */ 
/*    */	String determination = EventManager.doEvents(Arrays.asList(new String[] { "hero changes class", "hero changes class to " + event.getTo().getName() }), npc, player, context).toUpperCase();	
/*    */     
/* 52 */
/* 57 */     if (determination.equals("CANCELLED"))
/* 58 */       event.setCancelled(true);
/*    */
/*    */    }
/*    */
/* 
          Event :  on hero leveled up:
          Context: - <context.level> Returns the current heroes level.
                   - <context.to>  Returns the level the hero got to.
                   - <context.from> Returns the level the hero came from.  */
/*    */
/*    */   @EventHandler
/*    */   public void changeLevel(HeroChangeLevelEvent event) {
/*    */   
/* 38 */     dEntity hero = new dEntity(event.getHero().getEntity());
/* 39 */     Player player = null;
/* 40 */     dNPC npc = null;
/* 42 */     
             Map<String, dObject> context = new HashMap<String, dObject>();
/* 43 */     context.put("level", new Element(event.getHero().getLevel()));
/*    */     context.put("to", new Element(event.getTo()));
/*    */     context.put("from", new Element(event.getFrom()));
/*    */
/*    */
/*    */
/* 47 */     if (hero.isNPC())
/* 48 */       npc = hero.getDenizenNPC();
/* 49 */     else if (hero.isPlayer()) {
/* 50 */       player = hero.getPlayer();
/*    */
/*    */     }
/*    */
/*    */
/* 52 */     String determination = EventManager.doEvents(Arrays.asList(new String[] { "hero leveled up", "hero leveled up to " + event.getHero().getLevel() }), npc, player, context).toUpperCase();
/*    */ 
/* 57 */     if (determination.equals("CANCELLED"))
/* 58 */       ((Cancellable) event).setCancelled(true);
/*    */     }
/*    */
/*
          Event :  on hero entered combat: 
          Context: - <context.attacker> Returns the damager.
                   - <context.damage>  Returns the dealt damage.
                   - <context.target> Returns the Entity target.  
                   - <context.reason> Returns the reason why the event got fired (Entity Atack for example) */
/*    */
/*    *
/*    */   @EventHandler
/*    */   public void enterCombat(HeroEnterCombatEvent event) {
/*    */      
/* 38 */     dEntity hero = new dEntity(event.getHero().getEntity());
/* 39 */     Player player = null;
/* 40 */     dNPC npc = null;
/*    */     
/* 42 */     
             Map<String, dObject> context = new HashMap<String, dObject>();
/* 43 */     context.put("reason", new Element(event.getReason().name()));
/*    */     context.put("damage", new Element(event.getTarget().getLastDamage()));
/*    */     context.put("target", new Element(event.getTarget().toString()));
/*    */     context.put("attacker", new Element(event.getHero().getName()));
/*    */
/*    */
/* 47 */     if (hero.isNPC())
/* 48 */       npc = hero.getDenizenNPC();
/* 49 */     else if (hero.isPlayer()) {
/* 50 */       player = hero.getPlayer();
/*    */
/*    */     }
/*    */
/* 52 */     String determination = EventManager.doEvents(Arrays.asList(new String[] { "hero entered combat", "hero entered combat at" + hero.getPlayer().getLocation() }), npc, player, context).toUpperCase();
/*    */ 
/* 57 */     if (determination.equals("CANCELLED"))
/* 58 */       ((Cancellable) event).setCancelled(true);
/*    */     
/*    */     }
/*    */
/*
          Event :  on hero used skill:  
          Context: - <context.caster> Returns the skill caster.
                   - <context.skill>  Returns the skill name.
                   - <context.healthcost> Returns the Healthcost of a skill.
                   - <context.healthcost> Returns the Manacost of a skill.    */
/*    */
/*    */
/*    */   @EventHandler
/*    */   public void useSkill(SkillUseEvent event) {
/*    */      
/* 38 */     dEntity hero = new dEntity(event.getHero().getEntity());
/* 39 */     Player player = null;
/* 40 */     dNPC npc = null;
/*    */     
/* 42 */     
             Map<String, dObject> context = new HashMap<String, dObject>();
/* 43 */     context.put("caster", new Element(event.getHero().getName()));
/*    */     context.put("skill", new Element(event.getSkill().getName()));
/*    */     context.put("healthcost", new Element(event.getHealthCost()));
/*    */     context.put("manacost", new Element(event.getManaCost()));
/* 47 */     if (hero.isNPC())
/* 48 */       npc = hero.getDenizenNPC();
/* 49 */     else if (hero.isPlayer()) {
/* 50 */       player = hero.getPlayer();
/*    */
/*    */     }
/*    */
/* 52 */     String determination = EventManager.doEvents(Arrays.asList(new String[] { "hero used skill", "hero used skill who" + hero.getPlayer() }), npc, player, context).toUpperCase();
/*    */ 
/* 57 */     if (determination.equals("CANCELLED"))
/* 58 */       ((Cancellable) event).setCancelled(true);
/*    */     }
/*    */
/*    */}
