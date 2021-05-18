package gameComponents;

import java.util.ArrayList;

import core.Component;
import core.components.ColliderComponent;
import main.DamageDealer;
import main.DamageReceivable;
import main.GameConstants;
import main.TeamBelonger;

/**
 * G�re la d�gats inflig�s et recus
 */
public class Hitbox extends Component implements TeamBelonger {
	
	public Hitbox()
	{
		dealDamages = true;
		receiveDamages = true;
		damages = 1;
		team = GameConstants.TEAM_ENNEMY;
	}
	
	public Hitbox(int team, boolean dealDamages, boolean receiveDamages, int damages)
	{
		this.team = team;
		this.dealDamages = dealDamages;
		this.receiveDamages = receiveDamages;
		this.damages = damages;
	}
	

	/**
	 * Equipe � laquelle appartient la hitbox
	 */
	public int team;
	
	/**
	 * D�gats inflig�s par cette hitbox
	 */
	public int damages = 1;
	
	/**
	 * Cette hitbox permet-elle d'infliger des d�gats
	 */
	public boolean dealDamages;
	
	/**
	 * Cette hitbox permet-elle de recevoir des d�gats
	 */
	public boolean receiveDamages;
	
	/**
	 * Liste des colliders qui compteront comme "hitbox"
	 */
	public ArrayList<ColliderComponent> colliders = new ArrayList<ColliderComponent>();
	
	/**
	 * Liste des callbacks en cas de d�gats recus
	 */
	public ArrayList<DamageReceivable> damageReceivedCallback = new ArrayList<DamageReceivable>();
	
	/**
	 * Liste des callbacks en cas de d�gats inflig�s
	 */
	public ArrayList<DamageDealer> damageDealtCallback = new ArrayList<DamageDealer>();
	
	
	
	@Override
	public void onTriggerEnter(ColliderComponent ownCollider, ColliderComponent otherCollider) {
		// TODO Auto-generated method stub
		super.onTriggerEnter(ownCollider, otherCollider);
		
		//Cette fonction n'est utile que si l'on inflige des d�gats
		if(!dealDamages)
			return;
		
		Hitbox otherHit = otherCollider.getOwner().getComponent(Hitbox.class);
		if(otherHit != null && otherHit != this && otherHit.receiveDamages && otherHit.colliders.contains(otherCollider) && this.getTeam()!=otherHit.getTeam())
		{
			//Si on est dans cette condition, alors on va infliger des dommages
			for(DamageReceivable cb : otherHit.damageReceivedCallback)
			{
				cb.receivedDamages(damages);
				for(DamageDealer dd : damageDealtCallback)
				{
					dd.dealtDamages(damages);
				}
			}
		}
	}
	
	@Override
	public void onTriggerExit(ColliderComponent ownCollider, ColliderComponent otherCollider) {
		// TODO Auto-generated method stub
		super.onTriggerExit(ownCollider, otherCollider);
	}

	@Override
	public int getTeam() {
		return this.team;
	}
}
