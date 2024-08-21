package net.sodiumstudio.befriendmobs.subsystems.baublesystem;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.nautils.containers.Tuple3;

/**
 * A {@code BaubleAttributeModifier} is a wrapped attribute modifier which is dynamically added and removed in bauble ticks. 
 * Unlike {@link AttributeModifier}s, although it contains an {@link AttributeModifier}, it's not single-instance, and will be dynamically created & removed during bauble ticks.
 */
public class BaubleAttributeModifier
{

	private final Predicate<BaubleProcessingArgs> ALWAYS = args -> true;
	
	/** Attribute modifier instance. */
	private final AttributeModifier modifier;
	
	/** Attribute to modify. */
	private final Attribute attribute;
	
	/** Additional condition to apply this modifier. Null = always true. */
	@Nullable
	private Predicate<BaubleProcessingArgs> additionalCondition = null;
	
	/** Bauble info this modifier is added from. */
	@Nullable
	private BaubleProcessingArgs appliedArgs = null;
	
	public BaubleAttributeModifier(Attribute attr, double amount, AttributeModifier.Operation operation)
	{
		UUID uuid = UUID.randomUUID();
		this.modifier = new AttributeModifier(uuid, uuid.toString(), amount, operation);
		this.attribute = attr;
	}
	
	public BaubleAttributeModifier setAdditionalCondition(Predicate<BaubleProcessingArgs> condition)
	{
		this.additionalCondition = condition;
		return this;
	}
	
	/** Put this BaubleModifier to a mob using given args. */
	public void addTo(BaubleProcessingArgs args)
	{
		args.getCapability().getModifiers().add(this);
		this.appliedArgs = args;
		this.tick();
	}
	
	/** Tick update */
	public void tick()
	{
		if (this.appliedArgs != null && this.appliedArgs.user() != null 
				&& this.appliedArgs.user() != null && this.appliedArgs.user().isAlive())
		{
			if (this.additionalCondition == null || this.additionalCondition.test(appliedArgs))
			{
				if (!this.appliedArgs.user().getAttribute(attribute).hasModifier(modifier))
					this.appliedArgs.user().getAttribute(attribute).addTransientModifier(modifier);
			}
			else
			{
				this.appliedArgs.user().getAttribute(attribute).removeModifier(modifier);
			}
		}
	}
	
	/** 
	 * Remove the AttributeModifier from the mob, but not removing this BaubleModifier from CBaubleEquippableMob modifier set.
	 * Invoked before clearing the BaubleModifier set in CBaubleEquippableMob on refreshing modifiers.
	 */
	public void clear()
	{
		if (this.appliedArgs != null && this.appliedArgs.user() != null 
				&& this.appliedArgs.user() != null && this.appliedArgs.user().isAlive())
			this.appliedArgs.user().getAttribute(attribute).removeModifier(modifier);
	}
	
	// ========== Utilities =================
	
	
	/**
	 * A quick builder for BaubleAttributeModifier array.
	 * <p>You can add the three properties (attribute, amount, operation) of each intended BaubleAttributeModifier in order, and it
	 * will automatically build BaubleAttributeModifier array.
	 * <p>Note: it doesn't support additional conditions. For additional conditions, use conventional array construction.
	 * <p>The simplest example: {@code makeModifier(Attributes.MAX_HEALTH, 3.0d, AttributeModifier.Operation.ADDITION);} creates
	 * a single-element BaubleAttributeModifier array with properties above.
	 * <p>For multiple-element, use: {@code makeModifier(Attributes.MAX_HEALTH, 3.0d, AttributeModifier.Operation.ADDITION, Attributes.ATTACK_DAMAGE, 1.0d, AttributeModifiers.ADDITION);}
	 * and it creates a 2-element array.
	 * <p>The AttributeModifier argument can accept various forms:
	 * <p>(a) {@link AttributeModifier.Operation} enum.
	 * <p>(b) String: "a", "add" or "addition" for ADDITION; "m", "mb", "multiply_base" for MULTIPLY_BASE;
	 * "mt" or "multiply_total" for MULTIPLY_TOTAL.
	 * <p>(c) Index: 0 for ADDITION, 1 for MULTIPLY_BASE, 2 for MULTIPLY_TOTAL.
	 * <p>(d) Omitted: ADDITION by default.
	 */
	public static BaubleAttributeModifier[] makeModifiers(Object... args)
	{
		ArrayList<BaubleAttributeModifier> outList = new ArrayList<>();
		int phase = 0;
		Tuple3<Attribute, Double, AttributeModifier.Operation> current = new Tuple3<>(null, null, null);
		for (int i = 0; i < args.length; ++i)
		{
			switch (phase)
			{
			case 0:
			{
				if (args[i] instanceof Attribute attr)
					current.a = attr;
				else throw new IllegalArgumentException("BaubleAttributeModifier#makeModifiers: Illegal argument at position " + Integer.toString(i)
						+ ". Expected: Attribute");
				phase = 1;
				break;
			}
			case 1:
			{
				if (args[i] instanceof Number num)
				{
					current.b = num.doubleValue();
				}
				else throw new IllegalArgumentException("BaubleAttributeModifier#makeModifiers: Illegal argument at position " + Integer.toString(i)
					+ ". Expected: double");
				phase = 2;
				// For the case last operation is omitted, complete the last properties and end
				if (i == args.length - 1)
				{
					current.c = AttributeModifier.Operation.ADDITION;
					phase = 0;
				}
				break;
			}
			case 2:
			{
				// Explicit value
				if (args[i] instanceof AttributeModifier.Operation ope)
				{
					current.c = ope;
				}
				// Parse string
				else if (args[i] instanceof String str)
				{
					String strl = str.toLowerCase();
					if (strl.equals("a") || strl.equals("add") || strl.equals("addition"))
						current.c = AttributeModifier.Operation.ADDITION;
					else if (strl.equals("m") || strl.equals("mb") || strl.equals("multiply_base") || strl.equals("multiply base") || strl.equals("multiplybase"))
						current.c = AttributeModifier.Operation.MULTIPLY_BASE;
					else if (strl.equals("mt") || strl.equals("multiply_total") || strl.equals("multiply total") || strl.equals("multiplytotal"))
						current.c = AttributeModifier.Operation.MULTIPLY_TOTAL;
					else throw new IllegalArgumentException("BaubleAttributeModifier#makeModifiers: Illegal string argument at position " + Integer.toString(i)
					+ ": \"" + str + "\"");
				}
				// Index
				else if (args[i] instanceof Number num)
				{
					switch (num.intValue())
					{
					case 0:
					{
						current.c = AttributeModifier.Operation.ADDITION;
						break;
					}
					case 1:
					{
						current.c = AttributeModifier.Operation.MULTIPLY_BASE;
						break;
					}
					case 2:
					{
						current.c = AttributeModifier.Operation.MULTIPLY_TOTAL;
						break;
					}
					default:
					{
						throw new IllegalArgumentException("BaubleAttributeModifier#makeModifiers: Illegal operation index argument at position " + Integer.toString(i)
						+ ": \"" + Integer.toString(num.intValue()) + "\". Expected 0, 1 or 2.");
					}
					}
				}
				// Omitted
				else if (args[i] instanceof Attribute)
				{
					current.c = AttributeModifier.Operation.ADDITION;
					i--;	// This attribute value needs to be re-accessed
				}
				else throw new IllegalArgumentException("BaubleAttributeModifier#makeModifiers: Illegal argument at position " + Integer.toString(i)
				+ ". Expected: AttributeModifier.Operation, String, Integer or Attribute/vararg end (for omitting)");
				
				phase = 0;
				break;
			}
			default:
			{
				throw new RuntimeException();
			}
			}
			if (current.a != null && current.b != null && current.c != null)
			{
				outList.add(new BaubleAttributeModifier(current.a, current.b, current.c));
				current.a = null;
				current.b = null;
				current.c = null;
				phase = 0;
			}
		}
		BaubleAttributeModifier[] out = new BaubleAttributeModifier[outList.size()];
		for (int i = 0; i < out.length; ++i)
			out[i] = outList.get(i);
		return out;
	}
	
	@Override
	public String toString() {
		return ("BaubleAttributeModifier{Attribute=" + this.attribute.getDescriptionId() + "; Amount=" + Double.toString(this.modifier.getAmount()) 
				+ "; Operation=" + this.modifier.getOperation().toString() + "}");
	}
}
