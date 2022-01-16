SELECT NAME
       FROM order_details od
       INNER JOIN products p ON p.code = od.product_id
       INNER JOIN orders ordr ON ordr.ID = od.ORDER_ID
	WHERE ordr.order_date >= DATE_ADD(CURDATE(), INTERVAL -10 DAY)
       GROUP BY NAME 
        DESC 
       LIMIT 3;