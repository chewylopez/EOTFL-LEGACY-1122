package com.chewylopez.echoofthefarlands.coremod;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class EOTFLTransformer implements IClassTransformer {

    private static final String TARGET_CLASS = "net.minecraft.world.gen.NoiseGeneratorOctaves";

    private static final String GENERATE_DESC = "([DIIIIIIDDD)[D";

    private static final String POPULATE_DESC = "([DDDDIIIDDDD)V";

    private static final String HOOK_OWNER =
            "com/chewylopez/echoofthefarlands/world/farlands/FarlandsNoiseHook";
    private static final String HOOK_NAME = "populateNoiseArray";
    private static final String HOOK_DESC =
            "(Lnet/minecraft/world/gen/NoiseGeneratorImproved;[DDDDIIIDDDDII)V";

    private static final int LOCAL_X_OFFSET = 2;
    private static final int LOCAL_Z_OFFSET = 4;

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null || !TARGET_CLASS.equals(transformedName)) {
            return basicClass;
        }

        ClassNode node = new ClassNode();
        new ClassReader(basicClass).accept(node, 0);

        int patched = 0;
        for (MethodNode method : node.methods) {
            if (!GENERATE_DESC.equals(method.desc)) {
                continue;
            }
            for (AbstractInsnNode insn : method.instructions.toArray()) {
                if (insn.getOpcode() != Opcodes.INVOKEVIRTUAL) {
                    continue;
                }
                MethodInsnNode call = (MethodInsnNode) insn;
                if (!POPULATE_DESC.equals(call.desc)) {
                    continue;
                }

                InsnList extraArgs = new InsnList();
                extraArgs.add(new VarInsnNode(Opcodes.ILOAD, LOCAL_X_OFFSET));
                extraArgs.add(new VarInsnNode(Opcodes.ILOAD, LOCAL_Z_OFFSET));
                method.instructions.insertBefore(call, extraArgs);
                method.instructions.set(call,
                        new MethodInsnNode(Opcodes.INVOKESTATIC, HOOK_OWNER, HOOK_NAME, HOOK_DESC, false));
                patched++;
            }
        }

        if (patched != 1) {
            System.err.println("[echoofthefarlands] FAILED to patch " + TARGET_CLASS
                    + " -- expected exactly 1 populateNoiseArray call, patched " + patched
                    + ". Far Lands generation will NOT be active.");
            return basicClass;
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        System.out.println("[echoofthefarlands] patched " + TARGET_CLASS + " noise wrap");
        return writer.toByteArray();
    }
}
