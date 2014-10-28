/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.MainGame;

import com.jme3.scene.Spatial;
import java.io.IOException;
import java.util.List;

/**
 * intefrace that abstract loading dice models
 * @author svt
 */
public interface ModelsLoader {
    List<Spatial> load()throws IOException;
}
