/*
 * Copyright (C) 2016 tkv
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.vesalainen.boat.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.Test;
import static org.junit.Assert.*;
import org.vesalainen.boat.server.pages.Page12;
import org.vesalainen.web.servlet.bean.Context;

/**
 *
 * @author tkv
 */
public class ModelTest
{
    
    public ModelTest()
    {
    }

    @Test
    public void test1() throws IOException, ClassNotFoundException
    {
        ThreadLocal<Context<Model>> threadLocal = new ThreadLocal<>();
        Model model = new Model(threadLocal);
        Context<Model> ctx = new Context(threadLocal, model);
        model.getPages().add(new Page12(threadLocal));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(ctx);
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Context<Model> ctx2 = (Context<Model>) ois.readObject();
        Model model2 = ctx2.getModel();
        
        assertEquals(model, model2);
        assertEquals(ctx2, ctx2.getThreadLocalModel().get());
        assertEquals(model2.getAddPage().getThreadLocalModel(), model2.getPages().get(0).getThreadLocalModel());
    }
    
}
