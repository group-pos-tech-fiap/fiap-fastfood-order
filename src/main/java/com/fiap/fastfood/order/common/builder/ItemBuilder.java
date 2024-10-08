package com.fiap.fastfood.order.common.builder;

import com.fiap.fastfood.order.core.entity.Item;
import com.fiap.fastfood.order.external.orm.ItemORM;

public class ItemBuilder {

    public static Item fromOrmToDomain(ItemORM orm) {
        return new Item()
                .setItemValue(orm.getItemValue())
                .setIdProduct(orm.getIdProduct())
                .setQuantity(orm.getQuantity());
    }

    public static ItemORM fromDomainToOrm(Item item) {
        return new ItemORM()
                .setItemValue(item.getItemValue())
                .setIdProduct(item.getIdProduct())
                .setQuantity(item.getQuantity());
    }
}
