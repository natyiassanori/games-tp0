/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.movement.behavior;

import br.cefetmg.games.movement.AlgoritmoMovimentacao;
import br.cefetmg.games.movement.Direcionamento;
import br.cefetmg.games.movement.Pose;
import com.badlogic.gdx.Input;
import java.util.Random;

/**
 *
 * @author Aluno
 */
public class Chegar extends AlgoritmoMovimentacao{
    
    double timeToTarget = 0.25;
    private static final char NOME = 'c';
    private float maxAngular = 30f;

    public Chegar(char nome, int radius) {
        super(nome);
        this.maxVelocidade=maxVelocidade;
    }

    @Override
    public Direcionamento guiar(Pose agente) {
        Direcionamento output = new Direcionamento();

        output.velocidade = super.alvo.getObjetivo().sub(agente.posicao);
        
        if(output.velocidade.len()>maxVelocidade){
            output.velocidade.nor();
            output.velocidade.scl(maxVelocidade);
        }
        
        agente.olharNaDirecaoDaVelocidade(output.velocidade);        
        output.rotacao=0;
        
        return output;
    }

    @Override
    public int getTeclaParaAtivacao() {
        return Input.Keys.C;
    }
    
}
